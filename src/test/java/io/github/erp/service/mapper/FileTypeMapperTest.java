package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileTypeMapperTest {

    private FileTypeMapper fileTypeMapper;

    @BeforeEach
    public void setUp() {
        fileTypeMapper = new FileTypeMapperImpl();
    }
}
