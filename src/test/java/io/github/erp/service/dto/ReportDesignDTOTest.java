package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
