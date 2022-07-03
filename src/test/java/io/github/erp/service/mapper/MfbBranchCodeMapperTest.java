package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MfbBranchCodeMapperTest {

    private MfbBranchCodeMapper mfbBranchCodeMapper;

    @BeforeEach
    public void setUp() {
        mfbBranchCodeMapper = new MfbBranchCodeMapperImpl();
    }
}
