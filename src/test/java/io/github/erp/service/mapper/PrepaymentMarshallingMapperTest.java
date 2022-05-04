package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrepaymentMarshallingMapperTest {

    private PrepaymentMarshallingMapper prepaymentMarshallingMapper;

    @BeforeEach
    public void setUp() {
        prepaymentMarshallingMapper = new PrepaymentMarshallingMapperImpl();
    }
}
