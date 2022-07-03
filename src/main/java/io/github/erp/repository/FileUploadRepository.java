package io.github.erp.repository;

import io.github.erp.domain.FileUpload;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FileUpload entity.
 */
@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long>, JpaSpecificationExecutor<FileUpload> {
    @Query(
        value = "select distinct fileUpload from FileUpload fileUpload left join fetch fileUpload.placeholders",
        countQuery = "select count(distinct fileUpload) from FileUpload fileUpload"
    )
    Page<FileUpload> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct fileUpload from FileUpload fileUpload left join fetch fileUpload.placeholders")
    List<FileUpload> findAllWithEagerRelationships();

    @Query("select fileUpload from FileUpload fileUpload left join fetch fileUpload.placeholders where fileUpload.id =:id")
    Optional<FileUpload> findOneWithEagerRelationships(@Param("id") Long id);
}
