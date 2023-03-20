package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusinessDocumentMapperTest {

    private BusinessDocumentMapper businessDocumentMapper;

    @BeforeEach
    public void setUp() {
        businessDocumentMapper = new BusinessDocumentMapperImpl();
    }
}
