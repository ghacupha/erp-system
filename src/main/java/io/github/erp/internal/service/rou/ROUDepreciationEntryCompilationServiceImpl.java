package io.github.erp.internal.service.rou;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.service.dto.LeasePeriodDTO;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import io.github.erp.service.dto.RouModelMetadataDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.github.erp.internal.service.rou.ROUCalculationUtils.calculate12MonthlyPeriodicityDepreciationAmount;

/**
 * This class compile the depreciation entries for each ROU model metadata
 * provided in the following way:
 * 1. We calculate the initial lease-period using the commencementDate of the
 * model-metadata provided
 * 2. Fetch a list of x lease-periods starting with the initial period computed
 * above and going through sequential increments until the leaseTerm specified
 * in the model metadata is attained
 * 3. For each period compute the depreciation amount using the straight line basis
 * and save a corresponding rou-depreciation-entry
 * <p>
 * After these steps the compilation for a single rouModelMetadata is complete
 */
@Transactional
@Service
public class ROUDepreciationEntryCompilationServiceImpl implements ROUDepreciationEntryCompilationService {

    private final InternalLeasePeriodService internalLeasePeriodService;

    public ROUDepreciationEntryCompilationServiceImpl(InternalLeasePeriodService internalLeasePeriodService) {
        this.internalLeasePeriodService = internalLeasePeriodService;
    }

    public List<RouDepreciationEntryDTO> compileDepreciationEntries(RouModelMetadataDTO model) {

        return internalLeasePeriodService.findLeasePeriods(model)
            .map(this::mapDepreciationEntryPeriod)
            .orElse(new ArrayList<>()) //  Blank List<RouDepreciationEntryDTO> just in case
            .stream()
            .map(entries -> updateMetadataValues(entries, model))
            .collect(Collectors.toUnmodifiableList());

    }

    private RouDepreciationEntryDTO updateMetadataValues(RouDepreciationEntryDTO entry, RouModelMetadataDTO modelMetadataDTO) {

        // TODO BigDecimal depreciationAmount = calculate12MonthlyPeriodicityDepreciationAmount(modelMetadataDTO.getLeaseAmount(), modelMetadataDTO.getCommencementDate(), modelMetadataDTO.getExpirationDate());
        entry.setDescription(entry.getLeasePeriod().getPeriodCode().concat(" ").concat(modelMetadataDTO.getModelTitle()).concat(" depreciation"));
        entry.setDepreciationAmount(modelMetadataDTO.getLeaseAmount().divide(BigDecimal.valueOf(modelMetadataDTO.getLeaseTermPeriods()), RoundingMode.HALF_EVEN).setScale(6, RoundingMode.HALF_EVEN));
        // TODO entry.setDepreciationAmount(depreciationAmount);
        entry.setOutstandingAmount(BigDecimal.ZERO);
        entry.setRouAssetIdentifier(modelMetadataDTO.getRouModelReference());
        entry.setRouDepreciationIdentifier(UUID.randomUUID());
        entry.setSequenceNumber(Math.toIntExact(entry.getLeasePeriod().getSequenceNumber()));
        entry.setActivated(true);
        entry.setDebitAccount(modelMetadataDTO.getDepreciationAccount());
        entry.setCreditAccount(modelMetadataDTO.getAssetAccount());
        entry.setAssetCategory(modelMetadataDTO.getAssetCategory());
        entry.setLeaseContract(modelMetadataDTO.getIfrs16LeaseContract());
        entry.setRouMetadata(modelMetadataDTO);

        return entry;
    }

    private List<RouDepreciationEntryDTO> mapDepreciationEntryPeriod(List<LeasePeriodDTO> leasePeriods) {

        return leasePeriods.stream().map(ROUDepreciationEntryCompilationServiceImpl::newPeriodicEntry)
            .collect(Collectors.toList());
    }

    private static RouDepreciationEntryDTO newPeriodicEntry(LeasePeriodDTO leasePeriodDTO) {
        RouDepreciationEntryDTO dto = new RouDepreciationEntryDTO();
        dto.setLeasePeriod(leasePeriodDTO);
        return dto;
    }
}
