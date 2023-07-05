package io.github.erp.erp.depreciation;

import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationMethodDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public class StraightLineDepreciationCalculator {

    public static BigDecimal calculateDepreciation(AssetRegistrationDTO asset, DepreciationPeriodDTO period, DepreciationMethodDTO depreciationMethod, AssetCategoryDTO assetCategory) {

        BigDecimal netBookValue = asset.getAssetCost();
        BigDecimal depreciationRate = getDepreciationRate();
        int elapsedMonths = calculateElapsedMonths(period);

        BigDecimal depreciationAmount = BigDecimal.ZERO;
        for (int month = 1; month <= elapsedMonths; month++) {
            BigDecimal monthlyDepreciation = netBookValue.multiply(depreciationRate);
            depreciationAmount = depreciationAmount.add(monthlyDepreciation);
            netBookValue = netBookValue.subtract(monthlyDepreciation);
            if (netBookValue.compareTo(BigDecimal.ZERO) < 0) {
                netBookValue = BigDecimal.ZERO;
            }
        }

        return depreciationAmount;
    }

    private static BigDecimal getDepreciationRate() {

        // TODO USE MODEL FOR RATES (DEPRECIATION METHOD)
        return BigDecimal.valueOf(0.03);
    }

    private static int calculateElapsedMonths(DepreciationPeriodDTO period) {
        // Calculate the number of elapsed months between the start of the period and the current date.
        // You can use appropriate date/time libraries to perform this calculation.
        // Here's a simplified example assuming each period is a month:
        LocalDate startDate = period.getStartDate();
        LocalDate currentDate = LocalDate.now();
        return Math.toIntExact(Period.between(startDate, currentDate).toTotalMonths());
    }
}

