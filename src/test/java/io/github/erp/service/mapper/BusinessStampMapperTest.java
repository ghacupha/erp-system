package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusinessStampMapperTest {

    private BusinessStampMapper businessStampMapper;

    @BeforeEach
    public void setUp() {
        businessStampMapper = new BusinessStampMapperImpl();
    }
}
