package io.github.erp.repository;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.ReportDesign;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReportDesign entity.
 */
@Repository
public interface ReportDesignRepository extends JpaRepository<ReportDesign, Long>, JpaSpecificationExecutor<ReportDesign> {
    @Query(
        value = "select distinct reportDesign from ReportDesign reportDesign left join fetch reportDesign.parameters left join fetch reportDesign.placeholders",
        countQuery = "select count(distinct reportDesign) from ReportDesign reportDesign"
    )
    Page<ReportDesign> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct reportDesign from ReportDesign reportDesign left join fetch reportDesign.parameters left join fetch reportDesign.placeholders"
    )
    List<ReportDesign> findAllWithEagerRelationships();

    @Query(
        "select reportDesign from ReportDesign reportDesign left join fetch reportDesign.parameters left join fetch reportDesign.placeholders where reportDesign.id =:id"
    )
    Optional<ReportDesign> findOneWithEagerRelationships(@Param("id") Long id);
}
