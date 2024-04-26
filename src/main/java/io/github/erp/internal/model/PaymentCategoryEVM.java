package io.github.erp.internal.model;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
