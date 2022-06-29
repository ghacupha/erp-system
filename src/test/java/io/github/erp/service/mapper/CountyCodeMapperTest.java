package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountyCodeMapperTest {

    private CountyCodeMapper countyCodeMapper;

    @BeforeEach
    public void setUp() {
        countyCodeMapper = new CountyCodeMapperImpl();
    }
}
