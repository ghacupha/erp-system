package io.github.erp.internal.repository;

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

import io.github.erp.domain.TAInterestPaidTransferRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the TAInterestPaidTransferRule entity.
 */
@Repository
public interface InternalTAInterestPaidTransferRuleRepository
    extends JpaRepository<TAInterestPaidTransferRule, Long>, JpaSpecificationExecutor<TAInterestPaidTransferRule> {
    @Query(
        value = "select distinct tAInterestPaidTransferRule from TAInterestPaidTransferRule tAInterestPaidTransferRule left join fetch tAInterestPaidTransferRule.placeholders",
        countQuery = "select count(distinct tAInterestPaidTransferRule) from TAInterestPaidTransferRule tAInterestPaidTransferRule"
    )
    Page<TAInterestPaidTransferRule> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct tAInterestPaidTransferRule from TAInterestPaidTransferRule tAInterestPaidTransferRule left join fetch tAInterestPaidTransferRule.placeholders"
    )
    List<TAInterestPaidTransferRule> findAllWithEagerRelationships();

    @Query(
        "select tAInterestPaidTransferRule from TAInterestPaidTransferRule tAInterestPaidTransferRule left join fetch tAInterestPaidTransferRule.placeholders where tAInterestPaidTransferRule.id =:id"
    )
    Optional<TAInterestPaidTransferRule> findOneWithEagerRelationships(@Param("id") Long id);
}
