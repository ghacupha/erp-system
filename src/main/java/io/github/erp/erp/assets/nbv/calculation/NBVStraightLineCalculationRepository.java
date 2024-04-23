package io.github.erp.erp.assets.nbv.calculation;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
import io.github.erp.domain.NetBookValueEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface NBVStraightLineCalculationRepository  extends JpaRepository<NetBookValueEntry, Long>, JpaSpecificationExecutor<NetBookValueEntry> {

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
            "        cat.depreciation_rate_yearly / (10000 * 12) AS monthly_depreciation_rate_decimal " +
            " FROM " +
            "   asset_registration ar " +
            "      LEFT JOIN asset_category cat ON ar.asset_category_id = cat.id " +
            "  WHERE " +
            "   ar.id = :asset_id " +
            ") " +
            "SELECT " +
            "  asset_cost - ( monthly_depreciation_rate_decimal * asset_cost * :period_n ) AS net_book_value_at_period_N " +
            "FROM " +
            "AssetDetails "
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
