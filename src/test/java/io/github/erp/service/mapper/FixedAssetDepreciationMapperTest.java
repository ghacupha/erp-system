package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FixedAssetDepreciationMapperTest {

    private FixedAssetDepreciationMapper fixedAssetDepreciationMapper;

    @BeforeEach
    public void setUp() {
        fixedAssetDepreciationMapper = new FixedAssetDepreciationMapperImpl();
    }
}
