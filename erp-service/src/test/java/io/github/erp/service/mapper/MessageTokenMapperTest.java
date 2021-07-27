package io.github.erp.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageTokenMapperTest {

    private MessageTokenMapper messageTokenMapper;

    @BeforeEach
    public void setUp() {
        messageTokenMapper = new MessageTokenMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(messageTokenMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(messageTokenMapper.fromId(null)).isNull();
    }
}
