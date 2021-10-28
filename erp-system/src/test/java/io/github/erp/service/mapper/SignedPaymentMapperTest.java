package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SignedPaymentMapperTest {

    private SignedPaymentMapper signedPaymentMapper;

    @BeforeEach
    public void setUp() {
        signedPaymentMapper = new SignedPaymentMapperImpl();
    }
}
