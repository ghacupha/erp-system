package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionAccountMapperTest {

    private TransactionAccountMapper transactionAccountMapper;

    @BeforeEach
    public void setUp() {
        transactionAccountMapper = new TransactionAccountMapperImpl();
    }
}
