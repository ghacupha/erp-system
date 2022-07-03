package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaxRuleMapperTest {

    private TaxRuleMapper taxRuleMapper;

    @BeforeEach
    public void setUp() {
        taxRuleMapper = new TaxRuleMapperImpl();
    }
}
