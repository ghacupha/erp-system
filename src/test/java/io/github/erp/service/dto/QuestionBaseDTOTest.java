package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionBaseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionBaseDTO.class);
        QuestionBaseDTO questionBaseDTO1 = new QuestionBaseDTO();
        questionBaseDTO1.setId(1L);
        QuestionBaseDTO questionBaseDTO2 = new QuestionBaseDTO();
        assertThat(questionBaseDTO1).isNotEqualTo(questionBaseDTO2);
        questionBaseDTO2.setId(questionBaseDTO1.getId());
        assertThat(questionBaseDTO1).isEqualTo(questionBaseDTO2);
        questionBaseDTO2.setId(2L);
        assertThat(questionBaseDTO1).isNotEqualTo(questionBaseDTO2);
        questionBaseDTO1.setId(null);
        assertThat(questionBaseDTO1).isNotEqualTo(questionBaseDTO2);
    }
}
