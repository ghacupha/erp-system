package io.github.erp.internal.repository;

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
import io.github.erp.domain.*;
import io.github.erp.internal.report.service.DepreciationEntryVM;
import io.github.erp.repository.DepreciationEntryReportItemRepository;
import io.github.erp.repository.DepreciationEntryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InternalDepreciationEntryItemRepository
    extends DepreciationEntryReportItemRepository,
    JpaRepository<DepreciationEntryReportItem, Long>,
    JpaSpecificationExecutor<DepreciationEntryReportItem> {

    Page<DepreciationEntryInternal> getDepreciationEntryByDepreciationPeriodEqualsAndAssetCategoryEqualsAndServiceOutletEquals(
        DepreciationPeriod depreciationPeriod, AssetCategory assetCategory, ServiceOutlet serviceOutlet, Pageable pageable
    );

    Page<DepreciationEntryInternal> getDepreciationEntryByDepreciationPeriodEqualsAndServiceOutletEquals(
        DepreciationPeriod depreciationPeriod, ServiceOutlet serviceOutlet, Pageable pageable
    );

    Page<DepreciationEntryInternal> getDepreciationEntryByDepreciationPeriodEqualsAndAssetCategoryEquals(
        DepreciationPeriod depreciationPeriod, AssetCategory assetCategory, Pageable pageable
    );

    @Query(value = "SELECT " +
        "     de.id as id, " +
        "     ar.asset_details as assetRegistrationDetails, " +
        "     posted_at as postedAt, " +
        "     de.asset_number as assetNumber, " +
        "     so.outlet_code as serviceOutlet, " +
        "     ac.asset_category_name as assetCategory, " +
        "     dm.depreciation_method_name as depreciationMethod, " +
        "     pd.period_code as depreciationPeriod, " +
        "     fm.fiscal_month_code as fiscalMonthCode, " +
        "     ar.asset_cost as assetRegistrationCost, " +
        "     de.depreciation_amount as depreciationAmount, " +
        "     de.elapsed_months as elapsedMonths, " +
        "     de.prior_months as priorMonths, " +
        "     de.useful_life_years as usefulLifeYears, " +
        "     de.previous_nbv as previousNBV, " +
        "     de.net_book_value as netBookValue, " +
        "     de.depreciation_period_start_date as depreciationPeriodStartDate, " +
        "     de.depreciation_period_end_date as depreciationPeriodEndDate, " +
        "     de.capitalization_date as capitalizationDate " +
        "FROM public.depreciation_entry de" +
        "  LEFT JOIN depreciation_period pd on depreciation_period_id = pd.id " +
        "  LEFT JOIN fiscal_month fm on de.fiscal_month_id = fm.id " +
        "  LEFT JOIN service_outlet so on de.service_outlet_id = so.id " +
        "  LEFT JOIN asset_category ac on de.asset_category_id = ac.id " +
        "  LEFT JOIN depreciation_method dm ON de.depreciation_method_id = dm.id" +
        "  LEFT JOIN asset_registration ar ON de.asset_registration_id = ar.id " +
        "WHERE " +
        "pd.id = :depreciationPeriodId", nativeQuery = true)
    Page<DepreciationEntryInternal> getDepreciationEntryByDepreciationPeriodEquals (
        @Param("depreciationPeriodId") Long depreciationPeriodId, Pageable pageable
    );
}
