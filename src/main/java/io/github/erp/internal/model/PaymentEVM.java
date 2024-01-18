package io.github.erp.internal.model;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;
import io.github.erp.internal.framework.ExcelViewModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEVM implements Serializable, ExcelViewModel<PaymentEVM> {

    @ExcelRow
    private Long rowIndex;

    @ExcelCell(0)
    private String paymentNumber;

    @ExcelCell(1)
    private String paymentDate;

    @ExcelCell(2)
    private double invoicedAmount;

    @ExcelCell(3)
    private double paymentAmount;

    @ExcelCell(4)
    private String description;

    @ExcelCell(5)
    private String settlementCurrency;

    @ExcelCell(6)
    private String dealerName;

    @ExcelCell(7)
    private String purchaseOrderNumber;

    private String fileUploadToken;

    @Override
    public String getFileUploadToken() {
        return fileUploadToken;
    }

    @Override
    public PaymentEVM getModelData() {
        return SerializationUtils.clone(this);
    }
}
