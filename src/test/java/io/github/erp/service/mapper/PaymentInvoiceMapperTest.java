package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentInvoiceMapperTest {

    private PaymentInvoiceMapper paymentInvoiceMapper;

    @BeforeEach
    public void setUp() {
        paymentInvoiceMapper = new PaymentInvoiceMapperImpl();
    }
}
