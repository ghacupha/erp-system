package io.github.erp.repository.search;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.LeasePaymentUpload;
import java.util.List;
import java.util.stream.Collectors;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.util.StringUtils;

/**
 * Spring Data Elasticsearch repository for the {@link LeasePaymentUpload} entity.
 */
public interface LeasePaymentUploadSearchRepository
    extends ElasticsearchRepository<LeasePaymentUpload, Long>, LeasePaymentUploadSearchRepositoryInternal {}

interface LeasePaymentUploadSearchRepositoryInternal {
    Page<LeasePaymentUpload> search(String query, Pageable pageable);

    Page<LeasePaymentUpload> search(String query, Long leaseContractId, Pageable pageable);
}

class LeasePaymentUploadSearchRepositoryInternalImpl implements LeasePaymentUploadSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    LeasePaymentUploadSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<LeasePaymentUpload> search(String query, Pageable pageable) {
        return search(query, null, pageable);
    }

    @Override
    public Page<LeasePaymentUpload> search(String query, Long leaseContractId, Pageable pageable) {
        BoolQueryBuilder boolQuery = boolQuery();

        if (StringUtils.hasText(query)) {
            boolQuery.must(queryStringQuery(query));
        } else {
            boolQuery.must(matchAllQuery());
        }

        if (leaseContractId != null) {
            boolQuery.filter(termQuery("leaseContract.id", leaseContractId));
        }

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(boolQuery).withPageable(pageable).build();
        List<LeasePaymentUpload> hits = elasticsearchTemplate
            .search(nativeSearchQuery, LeasePaymentUpload.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
