package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrepaymentAccountMapperTest {

    private PrepaymentAccountMapper prepaymentAccountMapper;

    @BeforeEach
    public void setUp() {
        prepaymentAccountMapper = new PrepaymentAccountMapperImpl();
    }
}
