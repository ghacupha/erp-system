package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobSheetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobSheet.class);
        JobSheet jobSheet1 = new JobSheet();
        jobSheet1.setId(1L);
        JobSheet jobSheet2 = new JobSheet();
        jobSheet2.setId(jobSheet1.getId());
        assertThat(jobSheet1).isEqualTo(jobSheet2);
        jobSheet2.setId(2L);
        assertThat(jobSheet1).isNotEqualTo(jobSheet2);
        jobSheet1.setId(null);
        assertThat(jobSheet1).isNotEqualTo(jobSheet2);
    }
}
