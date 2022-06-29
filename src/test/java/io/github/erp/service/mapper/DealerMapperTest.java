package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DealerMapperTest {

    private DealerMapper dealerMapper;

    @BeforeEach
    public void setUp() {
        dealerMapper = new DealerMapperImpl();
    }
}
