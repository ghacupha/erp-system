package io.github.erp.service.dto;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

class ProfessionalQualificationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalQualificationDTO.class);
        ProfessionalQualificationDTO professionalQualificationDTO1 = new ProfessionalQualificationDTO();
        professionalQualificationDTO1.setId(1L);
        ProfessionalQualificationDTO professionalQualificationDTO2 = new ProfessionalQualificationDTO();
        assertThat(professionalQualificationDTO1).isNotEqualTo(professionalQualificationDTO2);
        professionalQualificationDTO2.setId(professionalQualificationDTO1.getId());
        assertThat(professionalQualificationDTO1).isEqualTo(professionalQualificationDTO2);
        professionalQualificationDTO2.setId(2L);
        assertThat(professionalQualificationDTO1).isNotEqualTo(professionalQualificationDTO2);
        professionalQualificationDTO1.setId(null);
        assertThat(professionalQualificationDTO1).isNotEqualTo(professionalQualificationDTO2);
    }
}
