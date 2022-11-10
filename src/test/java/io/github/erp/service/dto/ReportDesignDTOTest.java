package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportDesignDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportDesignDTO.class);
        ReportDesignDTO reportDesignDTO1 = new ReportDesignDTO();
        reportDesignDTO1.setId(1L);
        ReportDesignDTO reportDesignDTO2 = new ReportDesignDTO();
        assertThat(reportDesignDTO1).isNotEqualTo(reportDesignDTO2);
        reportDesignDTO2.setId(reportDesignDTO1.getId());
        assertThat(reportDesignDTO1).isEqualTo(reportDesignDTO2);
        reportDesignDTO2.setId(2L);
        assertThat(reportDesignDTO1).isNotEqualTo(reportDesignDTO2);
        reportDesignDTO1.setId(null);
        assertThat(reportDesignDTO1).isNotEqualTo(reportDesignDTO2);
    }
}
