package io.github.erp.repository;

import io.github.erp.domain.MessageToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MessageToken entity.
 */
@Repository
public interface MessageTokenRepository extends JpaRepository<MessageToken, Long>, JpaSpecificationExecutor<MessageToken> {
    @Query(
        value = "select distinct messageToken from MessageToken messageToken left join fetch messageToken.placeholders",
        countQuery = "select count(distinct messageToken) from MessageToken messageToken"
    )
    Page<MessageToken> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct messageToken from MessageToken messageToken left join fetch messageToken.placeholders")
    List<MessageToken> findAllWithEagerRelationships();

    @Query("select messageToken from MessageToken messageToken left join fetch messageToken.placeholders where messageToken.id =:id")
    Optional<MessageToken> findOneWithEagerRelationships(@Param("id") Long id);
}
