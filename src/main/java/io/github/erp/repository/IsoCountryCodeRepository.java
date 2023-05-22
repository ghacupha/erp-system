package io.github.erp.repository;

import io.github.erp.domain.IsoCountryCode;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the IsoCountryCode entity.
 */
@Repository
public interface IsoCountryCodeRepository extends JpaRepository<IsoCountryCode, Long>, JpaSpecificationExecutor<IsoCountryCode> {
    @Query(
        value = "select distinct isoCountryCode from IsoCountryCode isoCountryCode left join fetch isoCountryCode.placeholders",
        countQuery = "select count(distinct isoCountryCode) from IsoCountryCode isoCountryCode"
    )
    Page<IsoCountryCode> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct isoCountryCode from IsoCountryCode isoCountryCode left join fetch isoCountryCode.placeholders")
    List<IsoCountryCode> findAllWithEagerRelationships();

    @Query(
        "select isoCountryCode from IsoCountryCode isoCountryCode left join fetch isoCountryCode.placeholders where isoCountryCode.id =:id"
    )
    Optional<IsoCountryCode> findOneWithEagerRelationships(@Param("id") Long id);
}
