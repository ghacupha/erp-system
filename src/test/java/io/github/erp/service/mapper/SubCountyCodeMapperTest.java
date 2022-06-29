package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubCountyCodeMapperTest {

    private SubCountyCodeMapper subCountyCodeMapper;

    @BeforeEach
    public void setUp() {
        subCountyCodeMapper = new SubCountyCodeMapperImpl();
    }
}
