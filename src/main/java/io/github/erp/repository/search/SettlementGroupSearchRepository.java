package io.github.erp.repository.search;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.SettlementGroup;
import io.github.erp.repository.SettlementGroupRepository;
import java.util.List;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link SettlementGroup} entity.
 */
public interface SettlementGroupSearchRepository extends ElasticsearchRepository<SettlementGroup, Long>, SettlementGroupSearchRepositoryInternal {}

interface SettlementGroupSearchRepositoryInternal {
    Page<SettlementGroup> search(String query, Pageable pageable);

    Page<SettlementGroup> search(org.springframework.data.elasticsearch.core.query.Query query);

    void index(SettlementGroup entity);
}

class SettlementGroupSearchRepositoryInternalImpl implements SettlementGroupSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final SettlementGroupRepository repository;

    SettlementGroupSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate, SettlementGroupRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<SettlementGroup> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
            .withQuery(queryStringQuery(query))
            .withPageable(pageable)
            .build();
        return search(nativeSearchQuery);
    }

    @Override
    public Page<SettlementGroup> search(org.springframework.data.elasticsearch.core.query.Query query) {
        SearchHits<SettlementGroup> searchHits = elasticsearchTemplate.search(query, SettlementGroup.class);
        List<SettlementGroup> hits = searchHits.map(SearchHit::getContent).stream().collect(Collectors.toList());
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(SettlementGroup entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }
}
