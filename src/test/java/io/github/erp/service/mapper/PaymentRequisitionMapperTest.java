package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentRequisitionMapperTest {

    private PaymentRequisitionMapper paymentRequisitionMapper;

    @BeforeEach
    public void setUp() {
        paymentRequisitionMapper = new PaymentRequisitionMapperImpl();
    }
}
