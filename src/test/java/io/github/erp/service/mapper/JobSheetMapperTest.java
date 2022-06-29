package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JobSheetMapperTest {

    private JobSheetMapper jobSheetMapper;

    @BeforeEach
    public void setUp() {
        jobSheetMapper = new JobSheetMapperImpl();
    }
}
