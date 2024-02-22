package io.github.erp.erp.assets.nbv;

import io.github.erp.erp.assets.nbv.model.NBVArtefact;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import io.github.erp.internal.repository.InternalNbvCompilationJobRepository;
import io.github.erp.internal.service.InternalDepreciationPeriodService;
import io.github.erp.repository.AssetRegistrationRepository;
import io.github.erp.service.dto.NetBookValueEntryDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class BatchSequenceNBVCompilationServiceImpl implements BatchSequenceNBVCompilationService {

    private final NBVCalculatorService nbvCalculatorService;
    private final AssetRegistrationRepository assetRegistrationRepository;
    private final NetBookValueUpdateService netBookValueUpdateService;
    private final InternalNbvCompilationJobRepository internalNbvCompilationJobRepository;

    private final InternalDepreciationPeriodService internalDepreciationPeriodService;

    public BatchSequenceNBVCompilationServiceImpl(
        @Qualifier("nbvCalculatorServiceMain") NBVCalculatorService nbvCalculatorService,
        AssetRegistrationRepository assetRegistrationRepository,
        NetBookValueUpdateService netBookValueUpdateService,
        InternalNbvCompilationJobRepository internalNbvCompilationJobRepository,
        InternalDepreciationPeriodService internalDepreciationPeriodService) {
        this.nbvCalculatorService = nbvCalculatorService;
        this.assetRegistrationRepository = assetRegistrationRepository;
        this.netBookValueUpdateService = netBookValueUpdateService;
        this.internalNbvCompilationJobRepository = internalNbvCompilationJobRepository;
        this.internalDepreciationPeriodService = internalDepreciationPeriodService;
    }

    public NBVBatchMessage compile(NBVBatchMessage nbvBatchMessage) {

        List<NetBookValueEntryDTO> nbvs = new ArrayList<>();

        internalNbvCompilationJobRepository
            .findOneWithEagerRelationships(nbvBatchMessage.getJobId())
            .ifPresent(compilationJob -> {
                internalDepreciationPeriodService
                    .findOne(compilationJob.getActivePeriod().getId())
                    .ifPresent(depreciationPeriod -> {
                        for (Long assetId : nbvBatchMessage.getAssetIds()) {

                            assetRegistrationRepository.findOneWithEagerRelationships(assetId).ifPresent(assetRegistration -> {

                                NBVArtefact nbvArtefact = nbvCalculatorService.calculateNetBookValue(assetRegistration, nbvBatchMessage, depreciationPeriod);

                                NetBookValueEntryDTO netBookValueEntryDTO = netBookValueUpdateService.netBookValueUpdate(assetRegistration, nbvBatchMessage, compilationJob, nbvArtefact);

                                nbvs.add(netBookValueEntryDTO);
                            });
                        }
                    });
            });

        // Running buffered persistence job
        netBookValueUpdateService.saveCalculatedEntries(nbvs);

        return nbvBatchMessage;
    }

}
