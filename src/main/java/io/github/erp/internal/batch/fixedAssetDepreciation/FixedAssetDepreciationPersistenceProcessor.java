package io.github.erp.internal.batch.fixedAssetDepreciation;

import com.google.common.collect.ImmutableList;
import io.github.erp.internal.framework.batch.ListProcessor;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.FixedAssetDepreciationEVM;
import io.github.erp.service.dto.FixedAssetDepreciationDTO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * List processor implementation that also adds additional data to the output, i.e. the file-processing-token
 */
public class FixedAssetDepreciationPersistenceProcessor implements ListProcessor<FixedAssetDepreciationEVM, FixedAssetDepreciationDTO> {


    private final Mapping<FixedAssetDepreciationEVM, FixedAssetDepreciationDTO> mapping;
    private final String uploadToken;

    @org.jetbrains.annotations.Contract(pure = true)
    public FixedAssetDepreciationPersistenceProcessor(Mapping<FixedAssetDepreciationEVM, FixedAssetDepreciationDTO> mapping, String uploadToken) {
        this.mapping = mapping;
        this.uploadToken = uploadToken;
    }

    @Override
    public List<FixedAssetDepreciationDTO> process(@NonNull List<FixedAssetDepreciationEVM> evms) throws Exception {
        return evms.stream().map(mapping::toValue2).peek(d -> d.setFileUploadToken(uploadToken)).collect(ImmutableList.toImmutableList());
    }
}
