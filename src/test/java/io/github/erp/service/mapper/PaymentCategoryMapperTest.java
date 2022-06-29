package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentCategoryMapperTest {

    private PaymentCategoryMapper paymentCategoryMapper;

    @BeforeEach
    public void setUp() {
        paymentCategoryMapper = new PaymentCategoryMapperImpl();
    }
}
