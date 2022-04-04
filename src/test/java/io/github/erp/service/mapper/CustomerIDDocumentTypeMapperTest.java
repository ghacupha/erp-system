package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerIDDocumentTypeMapperTest {

    private CustomerIDDocumentTypeMapper customerIDDocumentTypeMapper;

    @BeforeEach
    public void setUp() {
        customerIDDocumentTypeMapper = new CustomerIDDocumentTypeMapperImpl();
    }
}
