package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportTemplateMapperTest {

    private ReportTemplateMapper reportTemplateMapper;

    @BeforeEach
    public void setUp() {
        reportTemplateMapper = new ReportTemplateMapperImpl();
    }
}
