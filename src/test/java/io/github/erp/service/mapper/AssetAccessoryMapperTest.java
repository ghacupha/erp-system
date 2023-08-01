package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetAccessoryMapperTest {

    private AssetAccessoryMapper assetAccessoryMapper;

    @BeforeEach
    public void setUp() {
        assetAccessoryMapper = new AssetAccessoryMapperImpl();
    }
}
