package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SettlementRequisitionMapperTest {

    private SettlementRequisitionMapper settlementRequisitionMapper;

    @BeforeEach
    public void setUp() {
        settlementRequisitionMapper = new SettlementRequisitionMapperImpl();
    }
}
