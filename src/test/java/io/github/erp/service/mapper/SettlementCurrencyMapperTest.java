package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SettlementCurrencyMapperTest {

    private SettlementCurrencyMapper settlementCurrencyMapper;

    @BeforeEach
    public void setUp() {
        settlementCurrencyMapper = new SettlementCurrencyMapperImpl();
    }
}
