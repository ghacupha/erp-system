package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportContentTypeMapperTest {

    private ReportContentTypeMapper reportContentTypeMapper;

    @BeforeEach
    public void setUp() {
        reportContentTypeMapper = new ReportContentTypeMapperImpl();
    }
}
