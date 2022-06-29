package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeliveryNoteMapperTest {

    private DeliveryNoteMapper deliveryNoteMapper;

    @BeforeEach
    public void setUp() {
        deliveryNoteMapper = new DeliveryNoteMapperImpl();
    }
}
