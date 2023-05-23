package io.github.erp.service.mapper;

import io.github.erp.domain.AgencyNotice;
import io.github.erp.service.dto.AgencyNoticeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AgencyNotice} and its DTO {@link AgencyNoticeDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { DealerMapper.class, SettlementCurrencyMapper.class, PlaceholderMapper.class, BusinessDocumentMapper.class }
)
public interface AgencyNoticeMapper extends EntityMapper<AgencyNoticeDTO, AgencyNotice> {
    @Mapping(target = "correspondents", source = "correspondents", qualifiedByName = "dealerNameSet")
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "assessor", source = "assessor", qualifiedByName = "dealerName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    AgencyNoticeDTO toDto(AgencyNotice s);

    @Mapping(target = "removeCorrespondents", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    AgencyNotice toEntity(AgencyNoticeDTO agencyNoticeDTO);
}
