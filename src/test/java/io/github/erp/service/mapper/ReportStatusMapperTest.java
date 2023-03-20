package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportStatusMapperTest {

    private ReportStatusMapper reportStatusMapper;

    @BeforeEach
    public void setUp() {
        reportStatusMapper = new ReportStatusMapperImpl();
    }
}
