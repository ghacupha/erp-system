package io.github.erp.internal.batch.dealer;

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
