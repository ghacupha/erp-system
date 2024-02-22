package io.github.erp.erp.assets.nbv;

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
        dto.setUsefulLifeYears(nbvArtefact.getUsefulLifeYears().intValueExact());
        dto.setNetBookValueAmount(nbvArtefact.getNetBookValueAmount());
        dto.setPreviousNetBookValueAmount(nbvArtefact.getPreviousNetBookValueAmount());
        /*TODO dto.setCapitalizationDate(nbvArtefact.getCapitalizationDate());*/
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
