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
import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.NbvCompilationJob;
import io.github.erp.domain.NetBookValueEntry;
import io.github.erp.erp.assets.nbv.buffer.BufferedSinkProcessor;
import io.github.erp.erp.assets.nbv.model.NBVArtefact;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import io.github.erp.service.dto.NetBookValueEntryDTO;
import io.github.erp.service.mapper.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

/**
 * Calculates and returns a transient but fully populated NBV entry
 */
@Transactional
@Service
public class NetBookValueUpdateServiceImpl implements NetBookValueUpdateService {

    private final FiscalMonthMapper fiscalMonthMapper;
    private final DepreciationMethodMapper depreciationMethodMapper;
    private final AssetRegistrationMapper assetRegistrationMapper;
    private final AssetCategoryMapper assetCategoryMapper;
    private final PlaceholderMapper placeholderMapper;
    private final DepreciationPeriodMapper depreciationPeriodMapper;

    private final BufferedSinkProcessor<NetBookValueEntry> netBookValueEntryBufferedSinkProcessor;
    private final NetBookValueEntryMapper netBookValueEntryMapper;
    private final ServiceOutletMapper serviceOutletMapper;

    public NetBookValueUpdateServiceImpl(
        FiscalMonthMapper fiscalMonthMapper,
        DepreciationMethodMapper depreciationMethodMapper,
        AssetRegistrationMapper assetRegistrationMapper,
        AssetCategoryMapper assetCategoryMapper,
        PlaceholderMapper placeholderMapper,
        DepreciationPeriodMapper depreciationPeriodMapper, BufferedSinkProcessor<NetBookValueEntry> netBookValueEntryBufferedSinkProcessor,
        NetBookValueEntryMapper netBookValueEntryMapper, ServiceOutletMapper serviceOutletMapper) {
        this.fiscalMonthMapper = fiscalMonthMapper;
        this.depreciationMethodMapper = depreciationMethodMapper;
        this.assetRegistrationMapper = assetRegistrationMapper;
        this.assetCategoryMapper = assetCategoryMapper;
        this.placeholderMapper = placeholderMapper;
        this.depreciationPeriodMapper = depreciationPeriodMapper;
        this.netBookValueEntryBufferedSinkProcessor = netBookValueEntryBufferedSinkProcessor;
        this.netBookValueEntryMapper = netBookValueEntryMapper;
        this.serviceOutletMapper = serviceOutletMapper;
    }

    public NetBookValueEntryDTO netBookValueUpdate(AssetRegistration assetRegistration, NBVBatchMessage nbvBatchMessage, NbvCompilationJob nbvCompilationJob, NBVArtefact nbvArtefact) {

        NetBookValueEntryDTO dto = new NetBookValueEntryDTO();

        dto.setAssetNumber(assetRegistration.getAssetNumber());
        dto.setAssetTag(assetRegistration.getAssetTag());
        dto.setAssetDescription(assetRegistration.getAssetDetails());
        dto.setNbvIdentifier(UUID.randomUUID());
        dto.setCompilationJobIdentifier(nbvCompilationJob.getCompilationJobIdentifier());
        dto.setCompilationBatchIdentifier(nbvBatchMessage.getMessageCorrelationId());
        dto.setElapsedMonths(Math.toIntExact(nbvArtefact.getElapsedMonths()));
        dto.setPriorMonths(Math.toIntExact(nbvArtefact.getPriorMonths()));
        dto.setUsefulLifeYears(nbvArtefact.getUsefulLifeYears().doubleValue());
        dto.setNetBookValueAmount(nbvArtefact.getNetBookValueAmount().setScale(2, RoundingMode.HALF_EVEN));
        // dto.setPreviousNetBookValueAmount(nbvArtefact.getPreviousNetBookValueAmount().setScale(2, RoundingMode.HALF_EVEN));

        if (nbvArtefact.getPreviousNetBookValueAmount() != null) {
            dto.setPreviousNetBookValueAmount(nbvArtefact.getPreviousNetBookValueAmount().setScale(2, RoundingMode.HALF_EVEN));
        } else {
            // Handling the case where previousNetBookValueAmount is null
            // This is when an asset has been purchased in the same depreciation-period
            // Alt check capitalization-date > depreciation-period.startDate
            dto.setPreviousNetBookValueAmount(assetRegistration.getAssetCost()); // We default to asset-cost
        }


        dto.setCapitalizationDate(nbvArtefact.getCapitalizationDate());
        dto.setFiscalMonth(fiscalMonthMapper.toDto(nbvCompilationJob.getActivePeriod().getFiscalMonth()));
        dto.setHistoricalCost(assetRegistration.getHistoricalCost());
        dto.setServiceOutlet(serviceOutletMapper.toDto(assetRegistration.getMainServiceOutlet()));
        dto.setDepreciationPeriod(depreciationPeriodMapper.toDto(nbvCompilationJob.getActivePeriod()));
        dto.setDepreciationMethod(depreciationMethodMapper.toDto(assetRegistration.getAssetCategory().getDepreciationMethod()));
        dto.setAssetRegistration(assetRegistrationMapper.toDto(assetRegistration));
        dto.setAssetCategory(assetCategoryMapper.toDto(assetRegistration.getAssetCategory()));
        dto.setPlaceholders(placeholderMapper.toDtoIdSet(assetRegistration.getPlaceholders()));

        return dto;
    }

    /**
     * Save transient entries to the database
     *
     * @param netBookValueEntries
     */
    @Override
    public void saveCalculatedEntries(List<NetBookValueEntryDTO> netBookValueEntries) {

        // Running buffered persistence
        netBookValueEntries.stream().map(netBookValueEntryMapper::toEntity).forEach(netBookValueEntryBufferedSinkProcessor::addEntry);
    }
}
