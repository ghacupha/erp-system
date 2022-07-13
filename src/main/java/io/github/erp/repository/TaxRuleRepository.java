package io.github.erp.repository;

/*-
 * Erp System - Mark II No 19 (Baruch Series)
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
import io.github.erp.domain.TaxRule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TaxRule entity.
 */
@Repository
public interface TaxRuleRepository extends JpaRepository<TaxRule, Long>, JpaSpecificationExecutor<TaxRule> {
    @Query(
        value = "select distinct taxRule from TaxRule taxRule left join fetch taxRule.placeholders",
        countQuery = "select count(distinct taxRule) from TaxRule taxRule"
    )
    Page<TaxRule> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct taxRule from TaxRule taxRule left join fetch taxRule.placeholders")
    List<TaxRule> findAllWithEagerRelationships();

    @Query("select taxRule from TaxRule taxRule left join fetch taxRule.placeholders where taxRule.id =:id")
    Optional<TaxRule> findOneWithEagerRelationships(@Param("id") Long id);
}
