package io.github.erp.erp.assets.depreciation;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.domain.NetBookValueEntry;
import io.github.erp.erp.assets.depreciation.model.DepreciationArtefact;
import io.github.erp.erp.assets.nbv.buffer.BufferedSinkProcessor;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationBatchSequenceDTO;
import io.github.erp.service.dto.DepreciationMethodDTO;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import io.github.erp.service.dto.FiscalMonthDTO;
import io.github.erp.service.dto.NetBookValueEntryDTO;
import io.github.erp.service.dto.ServiceOutletDTO;
import io.github.erp.service.mapper.NetBookValueEntryMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepreciationNetBookValueService {

    private final NetBookValueEntryMapper netBookValueEntryMapper;
    private final BufferedSinkProcessor<NetBookValueEntry> netBookValueEntryBufferedSinkProcessor;

    public DepreciationNetBookValueService(
        NetBookValueEntryMapper netBookValueEntryMapper,
        BufferedSinkProcessor<NetBookValueEntry> netBookValueEntryBufferedSinkProcessor
    ) {
        this.netBookValueEntryMapper = netBookValueEntryMapper;
        this.netBookValueEntryBufferedSinkProcessor = netBookValueEntryBufferedSinkProcessor;
    }

    public NetBookValueEntryDTO enqueueNetBookValueEntry(
        DepreciationArtefact depreciationArtefact,
        AssetRegistrationDTO assetRegistration,
        DepreciationPeriodDTO depreciationPeriod,
        FiscalMonthDTO fiscalMonth,
        DepreciationMethodDTO depreciationMethod,
        AssetCategoryDTO assetCategory,
        ServiceOutletDTO serviceOutlet,
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO
    ) {
        NetBookValueEntryDTO dto = new NetBookValueEntryDTO();

        Optional.ofNullable(assetRegistration).ifPresent(asset -> {
            dto.setAssetNumber(asset.getAssetNumber());
            dto.setAssetTag(asset.getAssetTag());
            dto.setAssetDescription(asset.getAssetDetails());
            dto.setHistoricalCost(Optional.ofNullable(asset.getHistoricalCost()).orElse(asset.getAssetCost()));
            dto.setCapitalizationDate(asset.getCapitalizationDate());
            dto.setAssetRegistration(asset);
            dto.setPlaceholders(asset.getPlaceholders());
        });

        dto.setNbvIdentifier(UUID.randomUUID());

        Optional.ofNullable(depreciationBatchSequenceDTO).ifPresent(batchSequence -> {
            dto.setCompilationJobIdentifier(batchSequence.getDepreciationJobIdentifier());
            dto.setCompilationBatchIdentifier(batchSequence.getDepreciationPeriodIdentifier());
        });

        Optional.ofNullable(depreciationArtefact.getElapsedMonths()).ifPresent(value -> dto.setElapsedMonths(Math.toIntExact(value)));
        Optional.ofNullable(depreciationArtefact.getPriorMonths()).ifPresent(value -> dto.setPriorMonths(Math.toIntExact(value)));
        Optional.ofNullable(depreciationArtefact.getUsefulLifeYears()).ifPresent(value -> dto.setUsefulLifeYears(value.doubleValue()));

        dto.setNetBookValueAmount(scaleAmount(depreciationArtefact.getNbv()));
        dto.setPreviousNetBookValueAmount(scaleAmount(depreciationArtefact.getNbvBeforeDepreciation()));

        Optional.ofNullable(depreciationArtefact.getCapitalizationDate()).ifPresent(dto::setCapitalizationDate);

        dto.setDepreciationPeriod(depreciationPeriod);
        dto.setFiscalMonth(fiscalMonth);
        dto.setDepreciationMethod(depreciationMethod);
        dto.setAssetCategory(assetCategory);
        dto.setServiceOutlet(serviceOutlet);

        NetBookValueEntry entry = netBookValueEntryMapper.toEntity(dto);
        netBookValueEntryBufferedSinkProcessor.addEntry(entry);
        return dto;
    }

    public void flushPendingEntries() {
        netBookValueEntryBufferedSinkProcessor.flushRemainingItems();
        netBookValueEntryBufferedSinkProcessor.flushStuckTaskComplete();
    }

    public void startupBuffer() {
        netBookValueEntryBufferedSinkProcessor.startup();
    }

    private BigDecimal scaleAmount(BigDecimal amount) {
        return Optional.ofNullable(amount).map(value -> value.setScale(2, RoundingMode.HALF_EVEN)).orElse(null);
    }
}
