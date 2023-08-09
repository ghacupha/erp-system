package io.github.erp.erp.depreciation.calculation;

/*-
 * Erp System - Mark IV No 4 (Ehud Series) Server ver 1.3.4
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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


import com.github.javafaker.Faker;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.AssetRegistrationDTO;

import java.math.BigDecimal;

public class TestAssetDataGenerator {

    private static final Faker faker = new Faker();

    public static AssetRegistrationDTO generateRandomAssetRegistration() {
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setAssetNumber(faker.number().digits(6));
        asset.setAssetTag(faker.lorem().word());
        asset.setAssetCost(new BigDecimal(faker.number().numberBetween(1000, 100000)));
        // Set other properties as needed
        return asset;
    }
}

