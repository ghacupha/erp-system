package io.github.erp.repository;

import io.github.erp.domain.PrepaymentAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PrepaymentAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrepaymentAccountRepository extends JpaRepository<PrepaymentAccount, Long>, JpaSpecificationExecutor<PrepaymentAccount> {}
