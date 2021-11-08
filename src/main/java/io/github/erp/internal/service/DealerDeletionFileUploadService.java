package io.github.erp.internal.service;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.framework.service.DeletionUploadService;
import io.github.erp.internal.model.DealerBEO;
import io.github.erp.service.DealerQueryService;
import io.github.erp.service.criteria.DealerCriteria;
import io.github.erp.service.dto.DealerDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.StringFilter;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class DealerDeletionFileUploadService implements DeletionUploadService<DealerBEO> {

    private final Mapping<DealerBEO, DealerDTO> batchEntityDTOMapping;
    private final DealerQueryService queryService;

    public DealerDeletionFileUploadService(Mapping<DealerBEO, DealerDTO> batchEntityDTOMapping, DealerQueryService queryService) {
        this.batchEntityDTOMapping = batchEntityDTOMapping;
        this.queryService = queryService;
    }

    @Override
    public Optional<List<DealerBEO>> findAllByUploadToken(String stringToken) {
        DealerCriteria criteria = new DealerCriteria();
        StringFilter uploadToken = new StringFilter();
        uploadToken.setEquals(stringToken);
        criteria.setFileUploadToken(uploadToken);
        return Optional.of(batchEntityDTOMapping.toValue1(queryService.findByCriteria(criteria)));
    }
}
