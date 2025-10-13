package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.DepreciationReport;
import io.github.erp.repository.DepreciationReportRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InternalDepreciationReportRepository
    extends DepreciationReportRepository,
    JpaRepository<DepreciationReport, Long>, JpaSpecificationExecutor<DepreciationReport> {


//    @Query("SELECT " +
//        "au " +
//        "FROM DepreciationReport dr " +
//        "LEFT JOIN FETCH dr.depreciationPeriod dp " +
//        "LEFT JOIN FETCH dr.requestedBy au " +
//        "LEFT JOIN FETCH dr.serviceOutlet so " +
//        "LEFT JOIN FETCH dr.assetCategory ac " +
//        "WHERE dr.id = :id")
//    @NotNull
//    Optional<DepreciationReport> fetchEagerlyById(@Param("id") @NotNull Long id);
}
