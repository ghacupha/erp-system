package io.github.erp.repository;

import io.github.erp.domain.QuestionBase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the QuestionBase entity.
 */
@Repository
public interface QuestionBaseRepository extends JpaRepository<QuestionBase, Long>, JpaSpecificationExecutor<QuestionBase> {
    @Query(
        value = "select distinct questionBase from QuestionBase questionBase left join fetch questionBase.parameters left join fetch questionBase.placeholderItems",
        countQuery = "select count(distinct questionBase) from QuestionBase questionBase"
    )
    Page<QuestionBase> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct questionBase from QuestionBase questionBase left join fetch questionBase.parameters left join fetch questionBase.placeholderItems"
    )
    List<QuestionBase> findAllWithEagerRelationships();

    @Query(
        "select questionBase from QuestionBase questionBase left join fetch questionBase.parameters left join fetch questionBase.placeholderItems where questionBase.id =:id"
    )
    Optional<QuestionBase> findOneWithEagerRelationships(@Param("id") Long id);
}
