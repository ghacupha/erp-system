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
public class SignedPaymentEVM implements Serializable, ExcelViewModel<SignedPaymentEVM> {

    @ExcelRow
    private Long rowIndex;

    @ExcelCell(0)
    private String transactionNumber;

    @ExcelCell(1)
    private String transactionDate;

    @ExcelCell(2)
    private String transactionCurrency;

    @ExcelCell(3)
    private double transactionAmount;

    @ExcelCell(4)
    private String dealerName;

    private String fileUploadToken;

    @Override
    public String getFileUploadToken() {
        return fileUploadToken;
    }

    @Override
    public SignedPaymentEVM getModelData() {
        return SerializationUtils.clone(this);
    }
}
