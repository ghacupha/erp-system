package io.github.erp.erp.assets.nbv;

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
