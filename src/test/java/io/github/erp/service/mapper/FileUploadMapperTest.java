package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileUploadMapperTest {

    private FileUploadMapper fileUploadMapper;

    @BeforeEach
    public void setUp() {
        fileUploadMapper = new FileUploadMapperImpl();
    }
}
