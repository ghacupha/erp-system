package io.github.erp.repository;

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
        value = "select distinct paymentCategory from PaymentCategory paymentCategory left join fetch paymentCategory.placeholders",
        countQuery = "select count(distinct paymentCategory) from PaymentCategory paymentCategory"
    )
    Page<PaymentCategory> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct paymentCategory from PaymentCategory paymentCategory left join fetch paymentCategory.placeholders")
    List<PaymentCategory> findAllWithEagerRelationships();

    @Query(
        "select paymentCategory from PaymentCategory paymentCategory left join fetch paymentCategory.placeholders where paymentCategory.id =:id"
    )
    Optional<PaymentCategory> findOneWithEagerRelationships(@Param("id") Long id);
}
