package io.github.erp.repository;

import io.github.erp.domain.Placeholder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Placeholder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaceholderRepository extends JpaRepository<Placeholder, Long>, JpaSpecificationExecutor<Placeholder> {}
