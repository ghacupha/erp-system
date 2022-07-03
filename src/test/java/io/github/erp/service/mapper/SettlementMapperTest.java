package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SettlementMapperTest {

    private SettlementMapper settlementMapper;

    @BeforeEach
    public void setUp() {
        settlementMapper = new SettlementMapperImpl();
    }
}
