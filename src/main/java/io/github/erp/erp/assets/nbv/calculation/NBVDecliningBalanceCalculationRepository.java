package io.github.erp.erp.assets.nbv.calculation;

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
import io.github.erp.domain.NetBookValueEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface NBVDecliningBalanceCalculationRepository extends JpaRepository<NetBookValueEntry, Long>, JpaSpecificationExecutor<NetBookValueEntry> {

    @Query(nativeQuery = true,
    value = "" +
        "WITH AssetDetails AS ( " +
        "    SELECT " +
        "        ar.asset_cost as asset_cost, " +
        "        cat.depreciation_rate_yearly as depreciation_rate " +
        "    FROM " +
        "        asset_registration ar " +
        "    left join asset_category cat on ar.asset_category_id = cat.id " +
        "    WHERE " +
        "        ar.id = :asset_id " +
        ") " +
        "SELECT " +
        "    asset_cost * (1 - POWER((1 - depreciation_rate/10000.0), :period_n )) AS depreciation_at_period_n " +
        "FROM " +
        "    AssetDetails;"
    )
    BigDecimal depreciationAtPeriodN(
        @Param("period_n") long periodN /*e.g. n = 5 */,
        @Param("asset_id") long assetId /*e.g. 65260*/
        );

    @Query(nativeQuery = true,
        value = "" +
            "WITH AssetDetails AS ( " +
            "    SELECT " +
            "        ar.asset_cost as asset_cost, " +
            "        cat.depreciation_rate_yearly as depreciation_rate " +
            "    FROM " +
            "        asset_registration ar " +
            "    left join asset_category cat on ar.asset_category_id = cat.id " +
            "    WHERE " +
            "        ar.id = :asset_id " +
            ") " +
            ", DepreciationPerPeriod AS ( " +
            "    SELECT " +
            "        generate_series(1, :period_n) as period, " + // Adjusting the range from 1 to 'N' for the specific period
            "        asset_cost * (1 - POWER((1 - depreciation_rate/10000), generate_series(1, :period_n))) AS depreciation_per_period " +
            "    FROM " +
            "        AssetDetails " +
            ") " +
            "SELECT " +
            "    sum(depreciation_per_period) as accrued_depreciation_at_period_N " +
            "FROM " +
            "    DepreciationPerPeriod;"
    )
    BigDecimal accruedDepreciationAtPeriodN(
        @Param("period_n") long periodN /* e.g. n = 5 */,
        @Param("asset_id") long assetId /* e.g. 65260 */
    );

    @Query( nativeQuery = true,
        value = "" +
            "WITH AssetDetails AS ( " +
            "    SELECT " +
            "        ar.asset_cost as asset_cost, " +
            "        cat.depreciation_rate_yearly / (10000 * 12) as monthly_depreciation_rate_decimal " +
            "    FROM " +
            "        public.asset_registration ar " +
            "    LEFT JOIN public.asset_category cat ON ar.asset_category_id = cat.id " +
            "    WHERE " +
            "        ar.id = :asset_id " +
            ") " +
            "SELECT " +
            "    asset_cost * POWER((1 - monthly_depreciation_rate_decimal), :period_n) AS net_book_value_at_period_N " +
            "FROM " +
            "    AssetDetails "
    )
    BigDecimal netBookValueAtPeriodN(
        @Param("period_n") long periodN /* e.g. n = 5 */,
        @Param("asset_id") long assetId /* e.g. 65260 */
    );

    @Query(nativeQuery = true,
        value = "" +
            "WITH AssetDetails AS ( " +
            "    SELECT " +
            "        ar.asset_cost AS asset_cost, " +
            "        cat.depreciation_rate_yearly AS depreciation_rate " +
            "    FROM " +
            "        asset_registration ar " +
            "      LEFT JOIN asset_category cat ON ar.asset_category_id = cat.id " +
            "    WHERE " +
            "        ar.id = :asset_id" + // Id of the asset
            "), " +
            "DepreciationPerPeriod AS ( " +
            "    SELECT " +
            "        generate_series(1, :period_n) AS period, " + //  -- Adjust the range from 1 to 'N' for the specific period" +
            "        asset_cost * (1 - POWER((1 - depreciation_rate/10000), generate_series(1, :period_n))) AS depreciation_per_period " +
            "    FROM " +
            "        AssetDetails" +
            "), " +
            "AccruedDepreciation AS ( " +
            "    SELECT " +
            "        sum(depreciation_per_period) AS accumulated_depreciation_at_period_N " +
            "    FROM " +
            "        DepreciationPerPeriod " +
            "), " +
            "NetBookValue AS ( " +
            "    SELECT " +
            "        " + // This is the known net book value
            "        :nbv AS net_book_value  " + // Replace with your actual net book value
            "), " +
            "CalculatedAssetCost AS ( " +
            "    SELECT " +
            "        accumulated_depreciation_at_period_N - net_book_value AS calculated_asset_cost " +
            "    FROM " +
            "        AccruedDepreciation, NetBookValue " +
            ") " +
            "SELECT " +
            "    calculated_asset_cost " +
            "FROM  " +
            "    CalculatedAssetCost; "
    )
    BigDecimal assetCostGivenNBVAtPeriodN(
        @Param("period_n") long periodN /* e.g. n = 5 */,
        @Param("asset_id") long assetId /* e.g. 65260 */,
        @Param("nbv") BigDecimal nbv /* e.g. 65260 */
    );
}
