package io.github.erp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PaymentCategoryMapperTest {

    private PaymentCategoryMapper paymentCategoryMapper;

    @BeforeEach
    public void setUp() {
        paymentCategoryMapper = new PaymentCategoryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(paymentCategoryMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(paymentCategoryMapper.fromId(null)).isNull();
    }
}
