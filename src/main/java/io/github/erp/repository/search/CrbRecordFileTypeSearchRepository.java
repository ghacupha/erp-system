package io.github.erp.repository.search;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.CrbRecordFileType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CrbRecordFileType} entity.
 */
public interface CrbRecordFileTypeSearchRepository
    extends ElasticsearchRepository<CrbRecordFileType, Long>, CrbRecordFileTypeSearchRepositoryInternal {}

interface CrbRecordFileTypeSearchRepositoryInternal {
    Page<CrbRecordFileType> search(String query, Pageable pageable);
}

class CrbRecordFileTypeSearchRepositoryInternalImpl implements CrbRecordFileTypeSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    CrbRecordFileTypeSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<CrbRecordFileType> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<CrbRecordFileType> hits = elasticsearchTemplate
            .search(nativeSearchQuery, CrbRecordFileType.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
