package io.github.erp.erp.depreciation.calculation;


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

