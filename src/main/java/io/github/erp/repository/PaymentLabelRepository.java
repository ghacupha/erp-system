package io.github.erp.repository;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import io.github.erp.domain.PaymentLabel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PaymentLabel entity.
 */
@Repository
public interface PaymentLabelRepository extends JpaRepository<PaymentLabel, Long>, JpaSpecificationExecutor<PaymentLabel> {
    @Query(
        value = "select distinct paymentLabel from PaymentLabel paymentLabel left join fetch paymentLabel.placeholders",
        countQuery = "select count(distinct paymentLabel) from PaymentLabel paymentLabel"
    )
    Page<PaymentLabel> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct paymentLabel from PaymentLabel paymentLabel left join fetch paymentLabel.placeholders")
    List<PaymentLabel> findAllWithEagerRelationships();

    @Query("select paymentLabel from PaymentLabel paymentLabel left join fetch paymentLabel.placeholders where paymentLabel.id =:id")
    Optional<PaymentLabel> findOneWithEagerRelationships(@Param("id") Long id);
}
