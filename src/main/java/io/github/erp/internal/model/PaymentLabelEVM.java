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
public class PaymentLabelEVM  implements Serializable, ExcelViewModel<PaymentLabelEVM> {

    @ExcelRow
    private Long rowIndex;

    @ExcelCell(0)
    private String description;

    @ExcelCell(1)
    private String comments;

    private String fileUploadToken;

    private String compilationToken;

    @Override
    public PaymentLabelEVM getModelData() {
        return SerializationUtils.clone(this);
    }
}
