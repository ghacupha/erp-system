package io.github.erp.internal.batch.fixedAssetAcquisition;

import com.google.common.collect.ImmutableList;
import io.github.erp.internal.framework.batch.ListProcessor;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.FixedAssetAcquisitionEVM;
import io.github.erp.service.dto.FixedAssetAcquisitionDTO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * List processor implementation that also adds additional data to the output, i.e. the file-processing-token
 */
public class FixedAssetAcquisitionPersistenceProcessor implements ListProcessor<FixedAssetAcquisitionEVM, FixedAssetAcquisitionDTO> {

    private final Mapping<FixedAssetAcquisitionEVM, FixedAssetAcquisitionDTO> mapping;
    private final String uploadToken;

    @org.jetbrains.annotations.Contract(pure = true)
    public FixedAssetAcquisitionPersistenceProcessor(Mapping<FixedAssetAcquisitionEVM, FixedAssetAcquisitionDTO> mapping, String uploadToken) {
        this.mapping = mapping;
        this.uploadToken = uploadToken;
    }

    @Override
    public List<FixedAssetAcquisitionDTO> process(@NonNull List<FixedAssetAcquisitionEVM> evms) throws Exception {
        return evms.stream().map(mapping::toValue2).peek(d -> d.setFileUploadToken(uploadToken)).collect(ImmutableList.toImmutableList());
    }
}
