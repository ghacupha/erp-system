package io.github.erp.repository.search;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 0.8.0
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
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.User;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the User entity.
 */
public interface UserSearchRepository extends ElasticsearchRepository<User, Long>, UserSearchRepositoryInternal {}

interface UserSearchRepositoryInternal {
    Stream<User> search(String query);
}

class UserSearchRepositoryInternalImpl implements UserSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    UserSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<User> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, User.class).map(SearchHit::getContent).stream();
    }
}
