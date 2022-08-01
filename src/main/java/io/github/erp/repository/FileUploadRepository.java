package io.github.erp.repository;

/*-
 * Erp System - Mark II No 22 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
