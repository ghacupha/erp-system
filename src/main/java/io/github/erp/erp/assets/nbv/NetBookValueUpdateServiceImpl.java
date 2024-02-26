package io.github.erp.erp.assets.nbv;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.NbvCompilationJob;
import io.github.erp.domain.NetBookValueEntry;
import io.github.erp.erp.assets.nbv.buffer.BufferedSinkProcessor;
import io.github.erp.erp.assets.nbv.model.NBVArtefact;
import io.github.erp.erp.assets.nbv.model.NBVBatchMessage;
import io.github.erp.service.dto.NetBookValueEntryDTO;
import io.github.erp.service.mapper.AssetCategoryMapper;
import io.github.erp.service.mapper.AssetRegistrationMapper;
import io.github.erp.service.mapper.DepreciationMethodMapper;
import io.github.erp.service.mapper.FiscalMonthMapper;
import io.github.erp.service.mapper.NetBookValueEntryMapper;
import io.github.erp.service.mapper.PlaceholderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final BufferedSinkProcessor<NetBookValueEntry> netBookValueEntryBufferedSinkProcessor;
    private final NetBookValueEntryMapper netBookValueEntryMapper;

    public NetBookValueUpdateServiceImpl(
        FiscalMonthMapper fiscalMonthMapper,
        DepreciationMethodMapper depreciationMethodMapper,
        AssetRegistrationMapper assetRegistrationMapper,
        AssetCategoryMapper assetCategoryMapper,
        PlaceholderMapper placeholderMapper,
        BufferedSinkProcessor<NetBookValueEntry> netBookValueEntryBufferedSinkProcessor,
        NetBookValueEntryMapper netBookValueEntryMapper) {
        this.fiscalMonthMapper = fiscalMonthMapper;
        this.depreciationMethodMapper = depreciationMethodMapper;
        this.assetRegistrationMapper = assetRegistrationMapper;
        this.assetCategoryMapper = assetCategoryMapper;
        this.placeholderMapper = placeholderMapper;
        this.netBookValueEntryBufferedSinkProcessor = netBookValueEntryBufferedSinkProcessor;
        this.netBookValueEntryMapper = netBookValueEntryMapper;
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
        dto.setNetBookValueAmount(nbvArtefact.getNetBookValueAmount());
        dto.setPreviousNetBookValueAmount(nbvArtefact.getPreviousNetBookValueAmount());
        dto.setCapitalizationDate(nbvArtefact.getCapitalizationDate());
        dto.setFiscalMonth(fiscalMonthMapper.toDto(nbvCompilationJob.getActivePeriod().getFiscalMonth()));
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
