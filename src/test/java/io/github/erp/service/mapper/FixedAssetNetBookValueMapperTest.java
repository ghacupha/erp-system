package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FixedAssetNetBookValueMapperTest {

    private FixedAssetNetBookValueMapper fixedAssetNetBookValueMapper;

    @BeforeEach
    public void setUp() {
        fixedAssetNetBookValueMapper = new FixedAssetNetBookValueMapperImpl();
    }
}
