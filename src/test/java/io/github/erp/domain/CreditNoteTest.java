package io.github.erp.domain;

/*-
 * Erp System - Mark II No 28 (Baruch Series) Server ver 0.0.8-SNAPSHOT
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

class CreditNoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CreditNote.class);
        CreditNote creditNote1 = new CreditNote();
        creditNote1.setId(1L);
        CreditNote creditNote2 = new CreditNote();
        creditNote2.setId(creditNote1.getId());
        assertThat(creditNote1).isEqualTo(creditNote2);
        creditNote2.setId(2L);
        assertThat(creditNote1).isNotEqualTo(creditNote2);
        creditNote1.setId(null);
        assertThat(creditNote1).isNotEqualTo(creditNote2);
    }
}
