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

import io.github.erp.domain.TAAmortizationRule;
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
 * Spring Data SQL repository for the TAAmortizationRule entity.
 */
@Repository
public interface InternalTAAmortizationRuleRepository
    extends JpaRepository<TAAmortizationRule, Long>, JpaSpecificationExecutor<TAAmortizationRule> {
    @Query(
        value = "select distinct tAAmortizationRule from TAAmortizationRule tAAmortizationRule left join fetch tAAmortizationRule.placeholders",
        countQuery = "select count(distinct tAAmortizationRule) from TAAmortizationRule tAAmortizationRule"
    )
    Page<TAAmortizationRule> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct tAAmortizationRule from TAAmortizationRule tAAmortizationRule left join fetch tAAmortizationRule.placeholders")
    List<TAAmortizationRule> findAllWithEagerRelationships();

    @Query(
        "select tAAmortizationRule from TAAmortizationRule tAAmortizationRule left join fetch tAAmortizationRule.placeholders where tAAmortizationRule.id =:id"
    )
    Optional<TAAmortizationRule> findOneWithEagerRelationships(@Param("id") Long id);
}
