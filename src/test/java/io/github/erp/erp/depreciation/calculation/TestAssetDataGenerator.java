package io.github.erp.erp.depreciation.calculation;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
import io.github.erp.service.dto.AssetRegistrationDTO;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

public class TestAssetDataGenerator {

    protected static final Faker faker = new Faker();

    public static AssetRegistrationDTO generateRandomAssetRegistration() {
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setAssetNumber(faker.number().digits(6));
        asset.setAssetTag(faker.lorem().word());
        asset.setAssetCost(new BigDecimal(faker.number().numberBetween(1000, 100000)));
        asset.setCapitalizationDate(
            faker.date().past(10000, TimeUnit.DAYS)
                .toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate()
        );
        // todo Set other properties as needed
        return asset;
    }
}

