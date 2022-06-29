package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankBranchCodeMapperTest {

    private BankBranchCodeMapper bankBranchCodeMapper;

    @BeforeEach
    public void setUp() {
        bankBranchCodeMapper = new BankBranchCodeMapperImpl();
    }
}
