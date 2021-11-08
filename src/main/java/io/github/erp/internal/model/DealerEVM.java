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
public class DealerEVM implements Serializable, ExcelViewModel<DealerEVM> {

    @ExcelRow
    private Long rowIndex;

    @ExcelCell(0)
    private String dealerName;

    @ExcelCell(1)
    private String taxNumber;

    @ExcelCell(2)
    private String postalAddress;

    @ExcelCell(3)
    private String physicalAddress;

    @ExcelCell(4)
    private String accountName;

    @ExcelCell(5)
    private String accountNumber;

    @ExcelCell(6)
    private String bankersName;

    @ExcelCell(7)
    private String bankersBranch;

    @ExcelCell(8)
    private String bankersSwiftCode;

    private String fileUploadToken;

    @Override
    public String getFileUploadToken() {
        return fileUploadToken;
    }

    @Override
    public DealerEVM getModelData() {
        return SerializationUtils.clone(this);
    }
}
