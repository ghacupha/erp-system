package io.github.erp.repository;

import io.github.erp.domain.PrepaymentAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PrepaymentAccount entity.
 */
@Repository
public interface PrepaymentAccountRepository extends JpaRepository<PrepaymentAccount, Long>, JpaSpecificationExecutor<PrepaymentAccount> {
    @Query(
        value = "select distinct prepaymentAccount from PrepaymentAccount prepaymentAccount left join fetch prepaymentAccount.placeholders left join fetch prepaymentAccount.generalParameters left join fetch prepaymentAccount.prepaymentParameters left join fetch prepaymentAccount.businessDocuments",
        countQuery = "select count(distinct prepaymentAccount) from PrepaymentAccount prepaymentAccount"
    )
    Page<PrepaymentAccount> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct prepaymentAccount from PrepaymentAccount prepaymentAccount left join fetch prepaymentAccount.placeholders left join fetch prepaymentAccount.generalParameters left join fetch prepaymentAccount.prepaymentParameters left join fetch prepaymentAccount.businessDocuments"
    )
    List<PrepaymentAccount> findAllWithEagerRelationships();

    @Query(
        "select prepaymentAccount from PrepaymentAccount prepaymentAccount left join fetch prepaymentAccount.placeholders left join fetch prepaymentAccount.generalParameters left join fetch prepaymentAccount.prepaymentParameters left join fetch prepaymentAccount.businessDocuments where prepaymentAccount.id =:id"
    )
    Optional<PrepaymentAccount> findOneWithEagerRelationships(@Param("id") Long id);
}
