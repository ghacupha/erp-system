package io.github.erp.repository.search;

/*-
 * Erp System - Mark II No 4 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
import java.util.ArrayList;
import java.util.List;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;

public class SortToFieldSortBuilderConverter implements Converter<Sort, List<FieldSortBuilder>> {

    @Override
    public List<FieldSortBuilder> convert(Sort sort) {
        List<FieldSortBuilder> builders = new ArrayList<>();
        sort
            .stream()
            .forEach(order -> {
                String property = order.getProperty() + ".keyword";
                SortOrder sortOrder = SortOrder.fromString(order.getDirection().name());
                builders.add(new FieldSortBuilder(property).order(sortOrder));
            });
        return builders;
    }
}
