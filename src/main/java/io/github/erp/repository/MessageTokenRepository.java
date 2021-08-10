package io.github.erp.repository;

import io.github.erp.domain.MessageToken;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MessageToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageTokenRepository extends JpaRepository<MessageToken, Long>, JpaSpecificationExecutor<MessageToken> {}
