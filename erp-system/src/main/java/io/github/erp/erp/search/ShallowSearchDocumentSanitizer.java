package io.github.erp.erp.search;

/*-
 * Erp System - Mark X No 12 (Kadar Series) Server ver 1.9.0
 * Copyright © 2021 - 2026 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds shallow search documents from JPA entities so Elasticsearch does not receive full relationship graphs.
 */
public final class ShallowSearchDocumentSanitizer {

    private static final Logger log = LoggerFactory.getLogger(ShallowSearchDocumentSanitizer.class);

    private static final Set<String> RELATION_DISPLAY_FIELDS = Set.of(
        "name",
        "description",
        "title",
        "code",
        "number",
        "bookingId",
        "dealerName",
        "currencyName",
        "iso4217CurrencyCode",
        "accountName",
        "accountNumber",
        "paymentNumber",
        "invoiceNumber",
        "storedFileName",
        "originalFileName",
        "email",
        "login"
    );

    private ShallowSearchDocumentSanitizer() {}

    public static <T> T sanitize(T entity) {
        if (entity == null || isSimpleValue(entity.getClass())) {
            return entity;
        }

        try {
            T document = instantiate(entity.getClass());
            copyRootFields(entity, document);
            return document;
        } catch (ReflectiveOperationException | SecurityException e) {
            log.warn("Could not create shallow search document for {}; indexing original entity", entity.getClass().getName(), e);
            return entity;
        }
    }

    private static <T> void copyRootFields(T source, T target) throws IllegalAccessException, ReflectiveOperationException {
        for (Field field : getAllFields(source.getClass())) {
            if (shouldSkipField(field)) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(source);

            if (value == null) {
                continue;
            }

            if (isToManyRelation(field) || isCollectionLike(field.getType())) {
                continue;
            }

            if (isToOneRelation(field)) {
                field.set(target, relationSummary(value));
                continue;
            }

            if (isSimpleValue(field.getType())) {
                field.set(target, value);
            }
        }
    }

    private static Object relationSummary(Object relation) throws ReflectiveOperationException {
        Object summary = instantiate(relation.getClass());

        for (Field field : getAllFields(relation.getClass())) {
            if (shouldSkipField(field) || isCollectionLike(field.getType()) || isToManyRelation(field) || isToOneRelation(field)) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(relation);

            if (value == null) {
                continue;
            }

            if ("id".equals(field.getName()) || (RELATION_DISPLAY_FIELDS.contains(field.getName()) && isSimpleValue(field.getType()))) {
                field.set(summary, value);
            }
        }

        return summary;
    }

    private static <T> T instantiate(Class<?> type) throws ReflectiveOperationException {
        Constructor<?> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        @SuppressWarnings("unchecked")
        T instance = (T) constructor.newInstance();
        return instance;
    }

    private static boolean shouldSkipField(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers) || field.isAnnotationPresent(Transient.class);
    }

    private static boolean isToManyRelation(Field field) {
        return field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToMany.class);
    }

    private static boolean isToOneRelation(Field field) {
        return field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class);
    }

    private static boolean isCollectionLike(Class<?> type) {
        return type.isArray() || Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type);
    }

    private static boolean isSimpleValue(Class<?> type) {
        return (
            type.isPrimitive() ||
            type.isEnum() ||
            String.class.equals(type) ||
            Number.class.isAssignableFrom(type) ||
            Boolean.class.equals(type) ||
            Character.class.equals(type) ||
            BigDecimal.class.equals(type) ||
            BigInteger.class.equals(type) ||
            UUID.class.equals(type) ||
            LocalDate.class.equals(type) ||
            LocalDateTime.class.equals(type) ||
            Instant.class.equals(type) ||
            ZonedDateTime.class.equals(type) ||
            OffsetDateTime.class.equals(type)
        );
    }

    private static Set<Field> getAllFields(Class<?> type) {
        Set<Field> fields = new HashSet<>();
        Class<?> current = type;

        while (current != null && !Object.class.equals(current)) {
            for (Field field : current.getDeclaredFields()) {
                fields.add(field);
            }
            current = current.getSuperclass();
        }

        return fields;
    }
}
