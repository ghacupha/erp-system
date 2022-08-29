package io.github.erp.config;

/*-
 * Erp System - Mark II No 27 (Baruch Series) Server ver 0.0.7-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

@Configuration
public class ElasticsearchConfiguration extends ElasticsearchConfigurationSupport {

    @Bean
    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(
            Arrays.asList(
                new ZonedDateTimeWritingConverter(),
                new ZonedDateTimeReadingConverter(),
                new InstantWritingConverter(),
                new InstantReadingConverter(),
                new LocalDateWritingConverter(),
                new LocalDateReadingConverter()
            )
        );
    }

    @WritingConverter
    static class ZonedDateTimeWritingConverter implements Converter<ZonedDateTime, String> {

        @Override
        public String convert(ZonedDateTime source) {
            if (source == null) {
                return null;
            }
            return source.toInstant().toString();
        }
    }

    @ReadingConverter
    static class ZonedDateTimeReadingConverter implements Converter<String, ZonedDateTime> {

        @Override
        public ZonedDateTime convert(String source) {
            if (source == null) {
                return null;
            }
            return Instant.parse(source).atZone(ZoneId.systemDefault());
        }
    }

    @WritingConverter
    static class InstantWritingConverter implements Converter<Instant, String> {

        @Override
        public String convert(Instant source) {
            if (source == null) {
                return null;
            }
            return source.toString();
        }
    }

    @ReadingConverter
    static class InstantReadingConverter implements Converter<String, Instant> {

        @Override
        public Instant convert(String source) {
            if (source == null) {
                return null;
            }
            return Instant.parse(source);
        }
    }

    @WritingConverter
    static class LocalDateWritingConverter implements Converter<LocalDate, String> {

        @Override
        public String convert(LocalDate source) {
            if (source == null) {
                return null;
            }
            return source.toString();
        }
    }

    @ReadingConverter
    static class LocalDateReadingConverter implements Converter<String, LocalDate> {

        @Override
        public LocalDate convert(String source) {
            if (source == null) {
                return null;
            }
            return LocalDate.parse(source);
        }
    }
}
