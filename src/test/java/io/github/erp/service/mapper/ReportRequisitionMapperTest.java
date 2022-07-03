package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportRequisitionMapperTest {

    private ReportRequisitionMapper reportRequisitionMapper;

    @BeforeEach
    public void setUp() {
        reportRequisitionMapper = new ReportRequisitionMapperImpl();
    }
}
