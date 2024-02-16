package io.github.erp.erp.assets.nbv;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface NBVStraightLineCalculationRepository {

    @Query(nativeQuery = true,
        value = "" +
            "WITH AssetDetails AS (" +
            "    SELECT " +
            "        ar.asset_cost AS asset_cost," +
            "        cat.depreciation_rate_yearly AS depreciation_rate_basis_points," +
            "        1000000.0 / cat.depreciation_rate_yearly AS inferred_useful_life" +
            "    FROM " +
            "        asset_registration ar " +
            "      LEFT JOIN asset_category cat ON ar.asset_category_id = cat.id " +
            "    WHERE " +
            "        ar.id = :asset_id" +
            ")," +
            "DepreciationPerPeriod AS (" +
            "    SELECT " +
            "        generate_series(1, inferred_useful_life) AS period, " +
            "        asset_cost / inferred_useful_life AS depreciation_per_period " +
            "    FROM " +
            "        AssetDetails " +
            ") " +
            "SELECT " +
            "    depreciation_per_period " +
            "FROM " +
            "    DepreciationPerPeriod " +
            "WHERE " +
            "    period = :period_n;")
    BigDecimal depreciationAtPeriodN(
        @Param("period_n") long periodN /*e.g. n = 5 */,
        @Param("asset_id") long assetId /*e.g. 65260*/
    );

    @Query(nativeQuery = true,
        value = "" +
            "WITH AssetDetails AS (" +
            "    SELECT " +
            "        ar.asset_cost AS asset_cost," +
            "        cat.depreciation_rate_yearly AS depreciation_rate_basis_points," +
            "        1000000.0 / cat.depreciation_rate_yearly AS inferred_useful_life" +
            "    FROM " +
            "        asset_registration ar " +
            "     LEFT JOIN asset_category cat ON ar.asset_category_id = cat.id " +
            "    WHERE " +
            "        ar.id = :asset_id" +
            ")," +
            "DepreciationPerPeriod AS (" +
            "    SELECT " +
            "        generate_series(1, inferred_useful_life) AS period," +
            "        asset_cost / inferred_useful_life AS depreciation_per_period" +
            "    FROM " +
            "        AssetDetails" +
            ")," +
            "AccumulatedDepreciation AS (" +
            "    SELECT " +
            "        sum(depreciation_per_period) AS accrued_depreciation_at_period_N" +
            "    FROM " +
            "        DepreciationPerPeriod" +
            "    WHERE" +
            "        period <= :period_n " + // Adjusting period_n in the param to the specific period you want to calculate the accrued depreciation for
            ") " +
            "SELECT " +
            "    accrued_depreciation_at_period_N " +
            "FROM " +
            "    AccumulatedDepreciation;"
    )
    BigDecimal accruedDepreciationAtPeriodN(
        @Param("period_n") long periodN /* e.g. n = 5 */,
        @Param("asset_id") long assetId /* e.g. 65260 */
    );

    @Query(nativeQuery = true,
        value = "" +
            "WITH AssetDetails AS ( " +
            "    SELECT " +
            "        ar.asset_cost AS asset_cost, " +
            "        cat.depreciation_rate_yearly AS depreciation_rate_basis_points, " +
            "        1000000.0 / cat.depreciation_rate_yearly AS inferred_useful_life " +
            "    FROM " +
            "        asset_registration ar " +
            "     LEFT JOIN asset_category cat ON ar.asset_category_id = cat.id " +
            "    WHERE " +
            "        ar.id = :asset_id " +
            "), " +
            "DepreciationPerPeriod AS (" +
            "    SELECT " +
            "        generate_series(1, inferred_useful_life) AS period, " +
            "        asset_cost / inferred_useful_life AS depreciation_per_period " +
            "    FROM " +
            "        AssetDetails " +
            ")," +
            "AccumulatedDepreciation AS (" +
            "    SELECT " +
            "        sum(depreciation_per_period) AS accrued_depreciation_at_period_N " +
            "    FROM " +
            "        DepreciationPerPeriod " +
            "    WHERE" +
            "        period <= :period_n " + // Adjusting period_n to the specific period you want to calculate the accrued depreciation for
            "), " +
            "NetBookValue AS (" +
            "    SELECT " +
            "        asset_cost - accrued_depreciation_at_period_N AS net_book_value_at_period_N " +
            "    FROM " +
            "        AssetDetails, AccumulatedDepreciation " +
            ") " +
            "SELECT " +
            "    net_book_value_at_period_N " +
            " FROM " +
            "    NetBookValue;"
    )
    BigDecimal netBookValueAtPeriodN(
        @Param("period_n") long periodN /* e.g. n = 5 */,
        @Param("asset_id") long assetId /* e.g. 65260 */
    );

    @Query(nativeQuery = true,
        value = "" +
            "WITH AssetDetails AS ( " +
            "    SELECT  " +
            "        ar.asset_cost AS asset_cost, " +
            "        cat.depreciation_rate_yearly AS depreciation_rate_basis_points, " +
            "        1000000.0 / cat.depreciation_rate_yearly AS inferred_useful_life " +
            "    FROM  " +
            "        asset_registration ar  " +
            "      LEFT JOIN asset_category cat ON ar.asset_category_id = cat.id  " +
            "    WHERE  " +
            "        ar.id = :asset_id " +
            "), " +
            "DepreciationPerPeriod AS ( " +
            "    SELECT  " +
            "        generate_series(1, inferred_useful_life) AS period, " +
            "        asset_cost / inferred_useful_life AS depreciation_per_period " +
            "    FROM  " +
            "        AssetDetails " +
            "), " +
            "AccumulatedDepreciation AS ( " +
            "    SELECT  " +
            "        sum(depreciation_per_period) AS accrued_depreciation_at_period_N " +
            "    FROM  " +
            "        DepreciationPerPeriod " +
            "    WHERE " +
            "        period <= :period_n "  + //-- Adjust '5' to the specific period you want to calculate the accrued depreciation for " +
            "), " +
            "NetBookValue AS ( " +
            "    SELECT  " +
            "        " + // Assume you have the net book value at period N "
            "        :nbv AS net_book_value_at_period_N " + // Replace nbv with the actual net book value
            "), " +
            "CalculatedAssetCost AS ( " +
            "    SELECT  " +
            "        accrued_depreciation_at_period_N + net_book_value_at_period_N AS calculated_asset_cost " +
            "    FROM  " +
            "        AccumulatedDepreciation, NetBookValue " +
            ") " +
            "SELECT  " +
            "    calculated_asset_cost " +
            "FROM  " +
            "    CalculatedAssetCost;"
    )
    BigDecimal assetCostGivenNBVAtPeriodN(
        @Param("period_n") long periodN /* e.g. n = 5 */,
        @Param("asset_id") long assetId /* e.g. 65260 */,
        @Param("nbv") BigDecimal nbv /* e.g. 65260 */
    );
}
