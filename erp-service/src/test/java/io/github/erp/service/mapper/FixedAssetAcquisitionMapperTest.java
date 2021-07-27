package io.github.erp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FixedAssetAcquisitionMapperTest {

    private FixedAssetAcquisitionMapper fixedAssetAcquisitionMapper;

    @BeforeEach
    public void setUp() {
        fixedAssetAcquisitionMapper = new FixedAssetAcquisitionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(fixedAssetAcquisitionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(fixedAssetAcquisitionMapper.fromId(null)).isNull();
    }
}
