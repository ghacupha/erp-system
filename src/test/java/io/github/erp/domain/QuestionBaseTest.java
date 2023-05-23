package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionBaseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionBase.class);
        QuestionBase questionBase1 = new QuestionBase();
        questionBase1.setId(1L);
        QuestionBase questionBase2 = new QuestionBase();
        questionBase2.setId(questionBase1.getId());
        assertThat(questionBase1).isEqualTo(questionBase2);
        questionBase2.setId(2L);
        assertThat(questionBase1).isNotEqualTo(questionBase2);
        questionBase1.setId(null);
        assertThat(questionBase1).isNotEqualTo(questionBase2);
    }
}
