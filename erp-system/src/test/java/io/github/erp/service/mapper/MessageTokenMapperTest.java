package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageTokenMapperTest {

    private MessageTokenMapper messageTokenMapper;

    @BeforeEach
    public void setUp() {
        messageTokenMapper = new MessageTokenMapperImpl();
    }
}
