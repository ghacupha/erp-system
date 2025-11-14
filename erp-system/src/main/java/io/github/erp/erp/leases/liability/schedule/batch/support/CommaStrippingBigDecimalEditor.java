package io.github.erp.erp.leases.liability.schedule.batch.support;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

/**
 * Parses {@link BigDecimal} values from strings that may contain thousand
 * separators. Empty strings are treated as {@code null} to allow optional
 * columns.
 */
public class CommaStrippingBigDecimalEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) {
        if (text == null || text.trim().isEmpty()) {
            setValue(null);
            return;
        }

        String sanitized = text.replace(",", "").trim();
        setValue(new BigDecimal(sanitized));
    }
}

