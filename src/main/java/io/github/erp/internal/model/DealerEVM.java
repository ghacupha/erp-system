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
