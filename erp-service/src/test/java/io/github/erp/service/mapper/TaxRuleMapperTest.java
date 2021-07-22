package io.github.erp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TaxRuleMapperTest {

    private TaxRuleMapper taxRuleMapper;

    @BeforeEach
    public void setUp() {
        taxRuleMapper = new TaxRuleMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(taxRuleMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(taxRuleMapper.fromId(null)).isNull();
    }
}
