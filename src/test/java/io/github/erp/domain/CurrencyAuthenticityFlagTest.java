package io.github.erp.domain;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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

class CurrencyAuthenticityFlagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyAuthenticityFlag.class);
        CurrencyAuthenticityFlag currencyAuthenticityFlag1 = new CurrencyAuthenticityFlag();
        currencyAuthenticityFlag1.setId(1L);
        CurrencyAuthenticityFlag currencyAuthenticityFlag2 = new CurrencyAuthenticityFlag();
        currencyAuthenticityFlag2.setId(currencyAuthenticityFlag1.getId());
        assertThat(currencyAuthenticityFlag1).isEqualTo(currencyAuthenticityFlag2);
        currencyAuthenticityFlag2.setId(2L);
        assertThat(currencyAuthenticityFlag1).isNotEqualTo(currencyAuthenticityFlag2);
        currencyAuthenticityFlag1.setId(null);
        assertThat(currencyAuthenticityFlag1).isNotEqualTo(currencyAuthenticityFlag2);
    }
}
