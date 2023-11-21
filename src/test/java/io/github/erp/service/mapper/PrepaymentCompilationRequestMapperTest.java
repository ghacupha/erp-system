package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrepaymentCompilationRequestMapperTest {

    private PrepaymentCompilationRequestMapper prepaymentCompilationRequestMapper;

    @BeforeEach
    public void setUp() {
        prepaymentCompilationRequestMapper = new PrepaymentCompilationRequestMapperImpl();
    }
}
