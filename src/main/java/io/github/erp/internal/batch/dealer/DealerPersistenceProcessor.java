package io.github.erp.internal.batch.dealer;

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
import com.google.common.collect.ImmutableList;
import io.github.erp.internal.framework.batch.ListProcessor;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.DealerEVM;
import io.github.erp.service.dto.DealerDTO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * List processor implementation that also adds additional data to the output, i.e. the file-processing-token
 */
public class DealerPersistenceProcessor implements ListProcessor<DealerEVM, DealerDTO> {

    private final Mapping<DealerEVM, DealerDTO> mapping;
    private final String uploadToken;

    @org.jetbrains.annotations.Contract(pure = true)
    public DealerPersistenceProcessor(Mapping<DealerEVM, DealerDTO> mapping, String uploadToken) {
        this.mapping = mapping;
        this.uploadToken = uploadToken;
    }

    @Override
    public List<DealerDTO> process(@NonNull List<DealerEVM> evms) throws Exception {
        return evms.stream().map(mapping::toValue2).peek(d -> d.setFileUploadToken(uploadToken)).collect(ImmutableList.toImmutableList());
    }
}
