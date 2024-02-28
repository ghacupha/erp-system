package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import io.github.erp.domain.AssetAdditionsReportItem;
import io.github.erp.domain.AssetAdditionsReportItemInternal;
import io.github.erp.repository.AssetAdditionsReportItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * Spring Data SQL repository for the AssetAdditionsReportItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalAssetAdditionsReportItemRepository
    extends AssetAdditionsReportItemRepository,
    JpaRepository<AssetAdditionsReportItem, Long>,
    JpaSpecificationExecutor<AssetAdditionsReportItem> {

    @Query(nativeQuery = true, value = "SELECT " +
        "   a.id as id, " +
        "   asset_number as assetNumber, " +
        "   asset_tag as assetTag, " +
        "   so.outlet_code as serviceOutletCode, " +
        "   settle.payment_number as transactionId, " +
        "   settle.payment_date as transactionDate, " +
        "   capitalization_date as capitalizationDate, " +
        "   ac.asset_category_name as assetCategory, " +
        "   asset_details as assetDetails, " +
        "   asset_cost as assetCost, " +
        "   supplier.dealer_name as supplier, " +
        "   historical_cost, " +
        "   registration_date   " +
        "FROM public.asset_registration a" +
        "  LEFT JOIN asset_category ac ON ac.id = asset_category_id " +
        "  LEFT JOIN dealer supplier ON supplier.id = dealer_id  " +
        "  LEFT JOIN settlement settle ON settle.id = acquiring_transaction_id " +
        "  LEFT JOIN service_outlet so ON so.id = main_service_outlet_id " +
        " WHERE capitalization_date between :reportPeriodStartDate and :reportPeriodEndDate ")
    Page<AssetAdditionsReportItemInternal> findAllByCapitalizationDate(
        @Param("reportPeriodStartDate") LocalDate reportPeriodStartDate,
        @Param("reportPeriodEndDate") LocalDate reportPeriodEndDate,
        Pageable pageable);
}
