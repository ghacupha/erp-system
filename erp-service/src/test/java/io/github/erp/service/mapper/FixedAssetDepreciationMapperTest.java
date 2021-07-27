package io.github.erp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FixedAssetDepreciationMapperTest {

    private FixedAssetDepreciationMapper fixedAssetDepreciationMapper;

    @BeforeEach
    public void setUp() {
        fixedAssetDepreciationMapper = new FixedAssetDepreciationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(fixedAssetDepreciationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(fixedAssetDepreciationMapper.fromId(null)).isNull();
    }
}
