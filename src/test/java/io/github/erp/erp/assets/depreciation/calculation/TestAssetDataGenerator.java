package io.github.erp.erp.assets.depreciation.calculation;

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

