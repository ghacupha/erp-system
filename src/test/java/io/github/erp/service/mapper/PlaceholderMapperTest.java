package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlaceholderMapperTest {

    private PlaceholderMapper placeholderMapper;

    @BeforeEach
    public void setUp() {
        placeholderMapper = new PlaceholderMapperImpl();
    }
}
