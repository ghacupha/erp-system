package io.github.erp.repository;

import io.github.erp.domain.AgencyNotice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AgencyNotice entity.
 */
@Repository
public interface AgencyNoticeRepository extends JpaRepository<AgencyNotice, Long>, JpaSpecificationExecutor<AgencyNotice> {
    @Query(
        value = "select distinct agencyNotice from AgencyNotice agencyNotice left join fetch agencyNotice.correspondents left join fetch agencyNotice.placeholders left join fetch agencyNotice.businessDocuments",
        countQuery = "select count(distinct agencyNotice) from AgencyNotice agencyNotice"
    )
    Page<AgencyNotice> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct agencyNotice from AgencyNotice agencyNotice left join fetch agencyNotice.correspondents left join fetch agencyNotice.placeholders left join fetch agencyNotice.businessDocuments"
    )
    List<AgencyNotice> findAllWithEagerRelationships();

    @Query(
        "select agencyNotice from AgencyNotice agencyNotice left join fetch agencyNotice.correspondents left join fetch agencyNotice.placeholders left join fetch agencyNotice.businessDocuments where agencyNotice.id =:id"
    )
    Optional<AgencyNotice> findOneWithEagerRelationships(@Param("id") Long id);
}
