package io.github.erp.repository;

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

import io.github.erp.domain.TALeaseInterestAccrualRule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TALeaseInterestAccrualRule entity.
 */
@Repository
public interface TALeaseInterestAccrualRuleRepository
    extends JpaRepository<TALeaseInterestAccrualRule, Long>, JpaSpecificationExecutor<TALeaseInterestAccrualRule> {
    @Query(
        value = "select distinct tALeaseInterestAccrualRule from TALeaseInterestAccrualRule tALeaseInterestAccrualRule left join fetch tALeaseInterestAccrualRule.placeholders",
        countQuery = "select count(distinct tALeaseInterestAccrualRule) from TALeaseInterestAccrualRule tALeaseInterestAccrualRule"
    )
    Page<TALeaseInterestAccrualRule> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct tALeaseInterestAccrualRule from TALeaseInterestAccrualRule tALeaseInterestAccrualRule left join fetch tALeaseInterestAccrualRule.placeholders"
    )
    List<TALeaseInterestAccrualRule> findAllWithEagerRelationships();

    @Query(
        "select tALeaseInterestAccrualRule from TALeaseInterestAccrualRule tALeaseInterestAccrualRule left join fetch tALeaseInterestAccrualRule.placeholders where tALeaseInterestAccrualRule.id =:id"
    )
    Optional<TALeaseInterestAccrualRule> findOneWithEagerRelationships(@Param("id") Long id);
}
