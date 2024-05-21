package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.domain.RouDepreciationEntry;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.config.web.servlet.PortMapperDsl;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data SQL repository for the RouDepreciationEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalRouDepreciationEntryRepository
    extends JpaRepository<RouDepreciationEntry, Long>, JpaSpecificationExecutor<RouDepreciationEntry> {

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT * " +
            "FROM public.rou_depreciation_entry " +
            "WHERE outstanding_amount <= :thresholdAmount",
        countQuery = "" +
            "SELECT * " +
            "FROM public.rou_depreciation_entry " +
            "WHERE outstanding_amount <= :thresholdAmount"
    )
    Optional<List<RouDepreciationEntry>> getOutstandingAmountItems(@Param("thresholdAmount") BigDecimal thresholdAmount);

    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT * " +
            "FROM public.rou_depreciation_entry " +
            "WHERE batch_job_identifier <= :batchJobIdentifier",
        countQuery = "" +
            "SELECT * " +
            "FROM public.rou_depreciation_entry " +
            "WHERE batch_job_identifier = :batchJobIdentifier"
    )
    Optional<List<RouDepreciationEntry>> getProcessedItems(@Param("batchJobIdentifier") UUID batchJobIdentifier);

    Optional<Integer> countRouDepreciationEntriesByBatchJobIdentifierEquals(UUID batchJobIdentifier);
}
