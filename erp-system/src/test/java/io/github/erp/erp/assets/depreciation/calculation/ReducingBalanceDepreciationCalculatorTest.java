package io.github.erp.erp.assets.depreciation.calculation;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.enumeration.DepreciationTypes;
import io.github.erp.erp.assets.depreciation.model.DepreciationArtefact;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationMethodDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static io.github.erp.erp.assets.depreciation.calculation.DepreciationConstants.MONEY_SCALE;
import static io.github.erp.erp.assets.depreciation.calculation.DepreciationConstants.ROUNDING_MODE;
import static io.github.erp.erp.assets.depreciation.calculation.DepreciationConstants.ZERO;

public class ReducingBalanceDepreciationCalculatorTest extends TestAssetDataGenerator {

    @Test
    void calculatesDepreciationWithoutPriorSnapshot() {
        AssetRegistrationDTO asset = generateRandomAssetRegistration();
        asset.setId(10L);
        asset.setAssetCost(new BigDecimal("58000").setScale(MONEY_SCALE, ROUNDING_MODE));
        asset.setCapitalizationDate(LocalDate.of(2023, 6, 6));

        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.DECLINING_BALANCE);

        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("3000"));

        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(LocalDate.of(2023, 6, 21));
        period.setEndDate(LocalDate.of(2023, 7, 20));

        NetBookValueSnapshotProvider provider = (assetId, startDate) -> Optional.empty();
        ReducingBalanceDepreciationCalculator calculator = new ReducingBalanceDepreciationCalculator(provider);

        DepreciationArtefact artefact = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod, ZERO);

        Assertions.assertEquals(new BigDecimal("1450.00"), artefact.getDepreciationAmount());
        Assertions.assertEquals(new BigDecimal("58000.00"), artefact.getNbvBeforeDepreciation());
        Assertions.assertEquals(new BigDecimal("56550.00"), artefact.getNbv());
    }

    @Test
    void calculatesDepreciationUsingPriorSnapshot() {
        AssetRegistrationDTO asset = generateRandomAssetRegistration();
        asset.setId(12L);
        asset.setAssetCost(new BigDecimal("58000").setScale(MONEY_SCALE, ROUNDING_MODE));
        asset.setCapitalizationDate(LocalDate.of(2023, 6, 6));

        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.DECLINING_BALANCE);

        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("3000"));

        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(LocalDate.of(2023, 6, 21));
        period.setEndDate(LocalDate.of(2023, 7, 20));

        NetBookValueSnapshotProvider provider = (assetId, startDate) -> Optional.of(new BigDecimal("56550.00"));
        ReducingBalanceDepreciationCalculator calculator = new ReducingBalanceDepreciationCalculator(provider);

        DepreciationArtefact artefact = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod, ZERO);

        Assertions.assertEquals(new BigDecimal("1413.75"), artefact.getDepreciationAmount());
        Assertions.assertEquals(new BigDecimal("56550.00"), artefact.getNbvBeforeDepreciation());
        Assertions.assertEquals(new BigDecimal("55136.25"), artefact.getNbv());
    }

    @Test
    void returnsZeroDepreciationWhenCapitalizedAfterPeriod() {
        AssetRegistrationDTO asset = generateRandomAssetRegistration();
        asset.setId(14L);
        asset.setAssetCost(new BigDecimal("89458.63").setScale(MONEY_SCALE, ROUNDING_MODE));
        asset.setCapitalizationDate(LocalDate.of(2024, 3, 24));

        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationType(DepreciationTypes.DECLINING_BALANCE);

        AssetCategoryDTO assetCategory = new AssetCategoryDTO();
        assetCategory.setDepreciationRateYearly(new BigDecimal("1250"));

        DepreciationPeriodDTO period = new DepreciationPeriodDTO();
        period.setStartDate(LocalDate.of(2023, 6, 21));
        period.setEndDate(LocalDate.of(2023, 7, 20));

        NetBookValueSnapshotProvider provider = (assetId, startDate) -> Optional.empty();
        ReducingBalanceDepreciationCalculator calculator = new ReducingBalanceDepreciationCalculator(provider);

        DepreciationArtefact artefact = calculator.calculateDepreciation(asset, period, assetCategory, depreciationMethod, ZERO);

        Assertions.assertEquals(new BigDecimal("0.00"), artefact.getDepreciationAmount());
        Assertions.assertEquals(new BigDecimal("89458.63"), artefact.getNbvBeforeDepreciation());
        Assertions.assertEquals(new BigDecimal("89458.63"), artefact.getNbv());
    }
}

