package io.github.erp.erp.assets.nbv;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.erp.assets.nbv.model.NBVArtefact;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import io.github.erp.internal.repository.InternalNbvCompilationJobRepository;
import io.github.erp.internal.service.assets.InternalDepreciationPeriodService;
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
