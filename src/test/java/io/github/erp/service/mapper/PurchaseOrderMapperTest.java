package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PurchaseOrderMapperTest {

    private PurchaseOrderMapper purchaseOrderMapper;

    @BeforeEach
    public void setUp() {
        purchaseOrderMapper = new PurchaseOrderMapperImpl();
    }
}
