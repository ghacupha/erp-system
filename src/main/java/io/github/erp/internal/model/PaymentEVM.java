package io.github.erp.internal.model;

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
