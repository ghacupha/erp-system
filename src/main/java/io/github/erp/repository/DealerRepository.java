package io.github.erp.repository;

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
import io.github.erp.domain.Dealer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dealer entity.
 */
@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long>, JpaSpecificationExecutor<Dealer> {
    @Query(
        value = "select distinct dealer from Dealer dealer left join fetch dealer.paymentLabels left join fetch dealer.placeholders",
        countQuery = "select count(distinct dealer) from Dealer dealer"
    )
    Page<Dealer> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct dealer from Dealer dealer left join fetch dealer.paymentLabels left join fetch dealer.placeholders")
    List<Dealer> findAllWithEagerRelationships();

    @Query("select dealer from Dealer dealer left join fetch dealer.paymentLabels left join fetch dealer.placeholders where dealer.id =:id")
    Optional<Dealer> findOneWithEagerRelationships(@Param("id") Long id);
}
