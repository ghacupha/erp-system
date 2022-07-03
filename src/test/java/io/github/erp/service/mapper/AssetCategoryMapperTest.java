package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetCategoryMapperTest {

    private AssetCategoryMapper assetCategoryMapper;

    @BeforeEach
    public void setUp() {
        assetCategoryMapper = new AssetCategoryMapperImpl();
    }
}
