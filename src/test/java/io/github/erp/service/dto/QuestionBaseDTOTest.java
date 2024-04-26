package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
