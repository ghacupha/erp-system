package io.github.erp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PaymentRequisitionMapperTest {

    private PaymentRequisitionMapper paymentRequisitionMapper;

    @BeforeEach
    public void setUp() {
        paymentRequisitionMapper = new PaymentRequisitionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(paymentRequisitionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(paymentRequisitionMapper.fromId(null)).isNull();
    }
}
