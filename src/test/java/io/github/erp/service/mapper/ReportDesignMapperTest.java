package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportDesignMapperTest {

    private ReportDesignMapper reportDesignMapper;

    @BeforeEach
    public void setUp() {
        reportDesignMapper = new ReportDesignMapperImpl();
    }
}