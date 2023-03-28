package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaseModelMetadataMapperTest {

    private LeaseModelMetadataMapper leaseModelMetadataMapper;

    @BeforeEach
    public void setUp() {
        leaseModelMetadataMapper = new LeaseModelMetadataMapperImpl();
    }
}
