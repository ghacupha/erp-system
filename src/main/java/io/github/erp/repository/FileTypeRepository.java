package io.github.erp.repository;

import io.github.erp.domain.FileType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FileType entity.
 */
@Repository
public interface FileTypeRepository extends JpaRepository<FileType, Long>, JpaSpecificationExecutor<FileType> {
    @Query(
        value = "select distinct fileType from FileType fileType left join fetch fileType.placeholders",
        countQuery = "select count(distinct fileType) from FileType fileType"
    )
    Page<FileType> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct fileType from FileType fileType left join fetch fileType.placeholders")
    List<FileType> findAllWithEagerRelationships();

    @Query("select fileType from FileType fileType left join fetch fileType.placeholders where fileType.id =:id")
    Optional<FileType> findOneWithEagerRelationships(@Param("id") Long id);
}
