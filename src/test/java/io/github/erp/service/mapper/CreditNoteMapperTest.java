package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreditNoteMapperTest {

    private CreditNoteMapper creditNoteMapper;

    @BeforeEach
    public void setUp() {
        creditNoteMapper = new CreditNoteMapperImpl();
    }
}
