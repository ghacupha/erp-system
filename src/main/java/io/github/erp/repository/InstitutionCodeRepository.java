package io.github.erp.repository;

import io.github.erp.domain.InstitutionCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InstitutionCode entity.
 */
@Repository
public interface InstitutionCodeRepository extends JpaRepository<InstitutionCode, Long>, JpaSpecificationExecutor<InstitutionCode> {
    @Query(
        value = "select distinct institutionCode from InstitutionCode institutionCode left join fetch institutionCode.placeholders",
        countQuery = "select count(distinct institutionCode) from InstitutionCode institutionCode"
    )
    Page<InstitutionCode> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct institutionCode from InstitutionCode institutionCode left join fetch institutionCode.placeholders")
    List<InstitutionCode> findAllWithEagerRelationships();

    @Query(
        "select institutionCode from InstitutionCode institutionCode left join fetch institutionCode.placeholders where institutionCode.id =:id"
    )
    Optional<InstitutionCode> findOneWithEagerRelationships(@Param("id") Long id);
}
