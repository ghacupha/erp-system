package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SecurityClearanceMapperTest {

    private SecurityClearanceMapper securityClearanceMapper;

    @BeforeEach
    public void setUp() {
        securityClearanceMapper = new SecurityClearanceMapperImpl();
    }
}
