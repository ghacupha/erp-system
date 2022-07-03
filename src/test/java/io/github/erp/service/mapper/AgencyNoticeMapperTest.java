package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgencyNoticeMapperTest {

    private AgencyNoticeMapper agencyNoticeMapper;

    @BeforeEach
    public void setUp() {
        agencyNoticeMapper = new AgencyNoticeMapperImpl();
    }
}
