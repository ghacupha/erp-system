package io.github.erp.internal.batch.fixedAssetNetBookValue;

import com.google.common.collect.ImmutableList;
import io.github.erp.internal.framework.batch.ListProcessor;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.FixedAssetNetBookValueEVM;
import io.github.erp.service.dto.FixedAssetNetBookValueDTO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * List processor implementation that also adds additional data to the output, i.e. the file-processing-token
 */
public class FANetBookValuePersistenceProcessor implements ListProcessor<FixedAssetNetBookValueEVM, FixedAssetNetBookValueDTO> {


    private final Mapping<FixedAssetNetBookValueEVM, FixedAssetNetBookValueDTO> mapping;
    private final String uploadToken;

    @org.jetbrains.annotations.Contract(pure = true)
    public FANetBookValuePersistenceProcessor(Mapping<FixedAssetNetBookValueEVM, FixedAssetNetBookValueDTO> mapping, String uploadToken) {
        this.mapping = mapping;
        this.uploadToken = uploadToken;
    }

    @Override
    public List<FixedAssetNetBookValueDTO> process(@NonNull List<FixedAssetNetBookValueEVM> evms) throws Exception {
        return evms.stream().map(mapping::toValue2).peek(d -> d.setFileUploadToken(uploadToken)).collect(ImmutableList.toImmutableList());
    }
}
