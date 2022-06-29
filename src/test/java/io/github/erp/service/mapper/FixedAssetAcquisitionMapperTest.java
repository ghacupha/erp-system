package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FixedAssetAcquisitionMapperTest {

    private FixedAssetAcquisitionMapper fixedAssetAcquisitionMapper;

    @BeforeEach
    public void setUp() {
        fixedAssetAcquisitionMapper = new FixedAssetAcquisitionMapperImpl();
    }
}
