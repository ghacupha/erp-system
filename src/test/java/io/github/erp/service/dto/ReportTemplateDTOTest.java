package io.github.erp.service.dto;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.5-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

class ReportTemplateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportTemplateDTO.class);
        ReportTemplateDTO reportTemplateDTO1 = new ReportTemplateDTO();
        reportTemplateDTO1.setId(1L);
        ReportTemplateDTO reportTemplateDTO2 = new ReportTemplateDTO();
        assertThat(reportTemplateDTO1).isNotEqualTo(reportTemplateDTO2);
        reportTemplateDTO2.setId(reportTemplateDTO1.getId());
        assertThat(reportTemplateDTO1).isEqualTo(reportTemplateDTO2);
        reportTemplateDTO2.setId(2L);
        assertThat(reportTemplateDTO1).isNotEqualTo(reportTemplateDTO2);
        reportTemplateDTO1.setId(null);
        assertThat(reportTemplateDTO1).isNotEqualTo(reportTemplateDTO2);
    }
}
