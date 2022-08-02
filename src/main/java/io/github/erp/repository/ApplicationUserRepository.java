package io.github.erp.repository;

import io.github.erp.domain.ApplicationUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApplicationUser entity.
 */
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long>, JpaSpecificationExecutor<ApplicationUser> {
    @Query(
        value = "select distinct applicationUser from ApplicationUser applicationUser left join fetch applicationUser.userProperties",
        countQuery = "select count(distinct applicationUser) from ApplicationUser applicationUser"
    )
    Page<ApplicationUser> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct applicationUser from ApplicationUser applicationUser left join fetch applicationUser.userProperties")
    List<ApplicationUser> findAllWithEagerRelationships();

    @Query(
        "select applicationUser from ApplicationUser applicationUser left join fetch applicationUser.userProperties where applicationUser.id =:id"
    )
    Optional<ApplicationUser> findOneWithEagerRelationships(@Param("id") Long id);
}
