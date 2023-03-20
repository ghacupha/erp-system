package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExcelReportExportMapperTest {

    private ExcelReportExportMapper excelReportExportMapper;

    @BeforeEach
    public void setUp() {
        excelReportExportMapper = new ExcelReportExportMapperImpl();
    }
}
