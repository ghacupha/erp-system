package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IsoCountryCodeMapperTest {

    private IsoCountryCodeMapper isoCountryCodeMapper;

    @BeforeEach
    public void setUp() {
        isoCountryCodeMapper = new IsoCountryCodeMapperImpl();
    }
}
