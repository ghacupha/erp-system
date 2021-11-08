package io.github.erp.internal.model;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;
import io.github.erp.domain.enumeration.CurrencyTypes;
import io.github.erp.internal.framework.ExcelViewModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceEVM implements Serializable, ExcelViewModel<InvoiceEVM> {

    @ExcelRow
    private Long rowIndex;

    @ExcelCell(0)
    private String invoiceNumber;

    @ExcelCell(1)
    private LocalDate invoiceDate;

    @ExcelCell(2)
    private BigDecimal invoiceAmount;

    @ExcelCell(3)
    private CurrencyTypes currency;

    private String fileUploadToken;

    @Override
    public String getFileUploadToken() {
        return fileUploadToken;
    }

    @Override
    public InvoiceEVM getModelData() {
        return SerializationUtils.clone(this);
    }
}
