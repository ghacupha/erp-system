package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaseContractMapperTest {

    private LeaseContractMapper leaseContractMapper;

    @BeforeEach
    public void setUp() {
        leaseContractMapper = new LeaseContractMapperImpl();
    }
}
