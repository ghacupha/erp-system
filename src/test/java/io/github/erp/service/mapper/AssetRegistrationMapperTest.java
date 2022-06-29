package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetRegistrationMapperTest {

    private AssetRegistrationMapper assetRegistrationMapper;

    @BeforeEach
    public void setUp() {
        assetRegistrationMapper = new AssetRegistrationMapperImpl();
    }
}
