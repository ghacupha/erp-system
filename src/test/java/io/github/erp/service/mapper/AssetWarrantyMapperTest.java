package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetWarrantyMapperTest {

    private AssetWarrantyMapper assetWarrantyMapper;

    @BeforeEach
    public void setUp() {
        assetWarrantyMapper = new AssetWarrantyMapperImpl();
    }
}
