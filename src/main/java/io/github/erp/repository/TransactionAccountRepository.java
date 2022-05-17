package io.github.erp.repository;

import io.github.erp.domain.TransactionAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TransactionAccount entity.
 */
@Repository
public interface TransactionAccountRepository
    extends JpaRepository<TransactionAccount, Long>, JpaSpecificationExecutor<TransactionAccount> {
    @Query(
        value = "select distinct transactionAccount from TransactionAccount transactionAccount left join fetch transactionAccount.placeholders",
        countQuery = "select count(distinct transactionAccount) from TransactionAccount transactionAccount"
    )
    Page<TransactionAccount> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct transactionAccount from TransactionAccount transactionAccount left join fetch transactionAccount.placeholders")
    List<TransactionAccount> findAllWithEagerRelationships();

    @Query(
        "select transactionAccount from TransactionAccount transactionAccount left join fetch transactionAccount.placeholders where transactionAccount.id =:id"
    )
    Optional<TransactionAccount> findOneWithEagerRelationships(@Param("id") Long id);
}
