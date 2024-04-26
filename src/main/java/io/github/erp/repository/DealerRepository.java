package io.github.erp.repository;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
