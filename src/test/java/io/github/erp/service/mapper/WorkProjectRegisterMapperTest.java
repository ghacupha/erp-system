package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkProjectRegisterMapperTest {

    private WorkProjectRegisterMapper workProjectRegisterMapper;

    @BeforeEach
    public void setUp() {
        workProjectRegisterMapper = new WorkProjectRegisterMapperImpl();
    }
}
