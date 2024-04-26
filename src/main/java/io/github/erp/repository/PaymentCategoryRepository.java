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
import io.github.erp.domain.PaymentCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PaymentCategory entity.
 */
@Repository
public interface PaymentCategoryRepository extends JpaRepository<PaymentCategory, Long>, JpaSpecificationExecutor<PaymentCategory> {
    @Query(
        value = "select distinct paymentCategory from PaymentCategory paymentCategory left join fetch paymentCategory.paymentLabels left join fetch paymentCategory.placeholders",
        countQuery = "select count(distinct paymentCategory) from PaymentCategory paymentCategory"
    )
    Page<PaymentCategory> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct paymentCategory from PaymentCategory paymentCategory left join fetch paymentCategory.paymentLabels left join fetch paymentCategory.placeholders"
    )
    List<PaymentCategory> findAllWithEagerRelationships();

    @Query(
        "select paymentCategory from PaymentCategory paymentCategory left join fetch paymentCategory.paymentLabels left join fetch paymentCategory.placeholders where paymentCategory.id =:id"
    )
    Optional<PaymentCategory> findOneWithEagerRelationships(@Param("id") Long id);
}
