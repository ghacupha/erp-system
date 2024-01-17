package io.github.erp.internal.repository;

import io.github.erp.domain.*;
import io.github.erp.internal.report.service.DepreciationEntryVM;
import io.github.erp.repository.DepreciationEntryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InternalDepreciationEntryRepository
    extends DepreciationEntryRepository,
    JpaRepository<DepreciationEntry, Long>,
    JpaSpecificationExecutor<DepreciationEntry> {

    Page<DepreciationEntry> getDepreciationEntryByDepreciationPeriodEqualsAndAssetCategoryEqualsAndServiceOutletEquals(
        DepreciationPeriod depreciationPeriod, AssetCategory assetCategory, ServiceOutlet serviceOutlet, Pageable pageable
    );

    Page<DepreciationEntry> getDepreciationEntryByDepreciationPeriodEqualsAndServiceOutletEquals(
        DepreciationPeriod depreciationPeriod, ServiceOutlet serviceOutlet, Pageable pageable
    );

    Page<DepreciationEntry> getDepreciationEntryByDepreciationPeriodEqualsAndAssetCategoryEquals(
        DepreciationPeriod depreciationPeriod, AssetCategory assetCategory, Pageable pageable
    );

    @Query(value = "SELECT " +
        "     de.id, " +
        "     ar.asset_details, " +
        "     posted_at, " +
        "     de.asset_number, " +
        "     so.outlet_code, " +
        "     ac.asset_category_name, " +
        "     dm.depreciation_method_name, " +
        "     pd.period_code," +
        "     fm.fiscal_month_code, " +
        "     ar.asset_cost," +
        "     de.depreciation_amount " +
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
