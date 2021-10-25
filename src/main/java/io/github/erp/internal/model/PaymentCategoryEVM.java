package io.github.erp.internal.model;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;
import io.github.erp.domain.enumeration.CategoryTypes;
import io.github.erp.internal.framework.ExcelViewModel;
import io.github.erp.service.dto.PaymentLabelDTO;
import io.github.erp.service.dto.PlaceholderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCategoryEVM implements Serializable, ExcelViewModel<PaymentCategoryEVM> {

    @ExcelRow
    private Long rowIndex;

    @ExcelCell(0)
    private String categoryName;

    @ExcelCell(1)
    private String categoryDescription;

    @ExcelCell(2)
    private String categoryType;

    private String fileUploadToken;

    @Override
    public PaymentCategoryEVM getModelData() {
        return SerializationUtils.clone(this);
    }
}
