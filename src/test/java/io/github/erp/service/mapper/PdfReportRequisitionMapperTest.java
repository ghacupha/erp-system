package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PdfReportRequisitionMapperTest {

    private PdfReportRequisitionMapper pdfReportRequisitionMapper;

    @BeforeEach
    public void setUp() {
        pdfReportRequisitionMapper = new PdfReportRequisitionMapperImpl();
    }
}
