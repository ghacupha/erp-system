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
import io.github.erp.domain.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Payment entity.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
    @Query(
        value = "select distinct payment from Payment payment left join fetch payment.paymentLabels left join fetch payment.placeholders",
        countQuery = "select count(distinct payment) from Payment payment"
    )
    Page<Payment> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct payment from Payment payment left join fetch payment.paymentLabels left join fetch payment.placeholders")
    List<Payment> findAllWithEagerRelationships();

    @Query(
        "select payment from Payment payment left join fetch payment.paymentLabels left join fetch payment.placeholders where payment.id =:id"
    )
    Optional<Payment> findOneWithEagerRelationships(@Param("id") Long id);
}
