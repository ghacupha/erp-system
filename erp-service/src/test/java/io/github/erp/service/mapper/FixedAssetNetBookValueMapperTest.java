package io.github.erp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FixedAssetNetBookValueMapperTest {

    private FixedAssetNetBookValueMapper fixedAssetNetBookValueMapper;

    @BeforeEach
    public void setUp() {
        fixedAssetNetBookValueMapper = new FixedAssetNetBookValueMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(fixedAssetNetBookValueMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(fixedAssetNetBookValueMapper.fromId(null)).isNull();
    }
}
