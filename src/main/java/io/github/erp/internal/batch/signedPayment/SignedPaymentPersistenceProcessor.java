
/*-
 * Erp System - Mark II No 19 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
package io.github.erp.internal.batch.signedPayment;

import com.google.common.collect.ImmutableList;
import io.github.erp.internal.framework.batch.ListProcessor;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.SignedPaymentEVM;
import io.github.erp.service.dto.SignedPaymentDTO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * List processor implementation that also adds additional data to the output, i.e. the file-processing-token
 */
public class SignedPaymentPersistenceProcessor implements ListProcessor<SignedPaymentEVM, SignedPaymentDTO> {


    private final Mapping<SignedPaymentEVM, SignedPaymentDTO> mapping;
    private final String uploadToken;

    @org.jetbrains.annotations.Contract(pure = true)
    public SignedPaymentPersistenceProcessor(Mapping<SignedPaymentEVM, SignedPaymentDTO> mapping, String uploadToken) {
        this.mapping = mapping;
        this.uploadToken = uploadToken;
    }

    @Override
    public List<SignedPaymentDTO> process(@NonNull List<SignedPaymentEVM> evms) throws Exception {
        return evms.stream().map(mapping::toValue2).peek(d -> d.setFileUploadToken(uploadToken)).collect(ImmutableList.toImmutableList());
    }
}
