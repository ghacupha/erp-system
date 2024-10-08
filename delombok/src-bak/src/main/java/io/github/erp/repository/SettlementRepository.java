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
import io.github.erp.domain.Settlement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Settlement entity.
 */
@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long>, JpaSpecificationExecutor<Settlement> {
    @Query(
        value = "select distinct settlement from Settlement settlement left join fetch settlement.placeholders left join fetch settlement.paymentLabels left join fetch settlement.paymentInvoices left join fetch settlement.signatories left join fetch settlement.businessDocuments",
        countQuery = "select count(distinct settlement) from Settlement settlement"
    )
    Page<Settlement> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct settlement from Settlement settlement left join fetch settlement.placeholders left join fetch settlement.paymentLabels left join fetch settlement.paymentInvoices left join fetch settlement.signatories left join fetch settlement.businessDocuments"
    )
    List<Settlement> findAllWithEagerRelationships();

    @Query(
        "select settlement from Settlement settlement left join fetch settlement.placeholders left join fetch settlement.paymentLabels left join fetch settlement.paymentInvoices left join fetch settlement.signatories left join fetch settlement.businessDocuments where settlement.id =:id"
    )
    Optional<Settlement> findOneWithEagerRelationships(@Param("id") Long id);
}
