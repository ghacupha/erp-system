package io.github.erp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TaxReferenceMapperTest {

    private TaxReferenceMapper taxReferenceMapper;

    @BeforeEach
    public void setUp() {
        taxReferenceMapper = new TaxReferenceMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(taxReferenceMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(taxReferenceMapper.fromId(null)).isNull();
    }
}
