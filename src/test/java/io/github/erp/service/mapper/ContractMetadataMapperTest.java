package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContractMetadataMapperTest {

    private ContractMetadataMapper contractMetadataMapper;

    @BeforeEach
    public void setUp() {
        contractMetadataMapper = new ContractMetadataMapperImpl();
    }
}
