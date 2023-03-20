package io.github.erp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionBaseMapperTest {

    private QuestionBaseMapper questionBaseMapper;

    @BeforeEach
    public void setUp() {
        questionBaseMapper = new QuestionBaseMapperImpl();
    }
}
