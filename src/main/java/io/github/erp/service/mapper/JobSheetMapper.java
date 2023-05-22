package io.github.erp.service.mapper;

import io.github.erp.domain.JobSheet;
import io.github.erp.service.dto.JobSheetDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link JobSheet} and its DTO {@link JobSheetDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        DealerMapper.class, BusinessStampMapper.class, PlaceholderMapper.class, PaymentLabelMapper.class, BusinessDocumentMapper.class,
    }
)
public interface JobSheetMapper extends EntityMapper<JobSheetDTO, JobSheet> {
    @Mapping(target = "biller", source = "biller", qualifiedByName = "dealerName")
    @Mapping(target = "signatories", source = "signatories", qualifiedByName = "dealerNameSet")
    @Mapping(target = "contactPerson", source = "contactPerson", qualifiedByName = "dealerName")
    @Mapping(target = "businessStamps", source = "businessStamps", qualifiedByName = "detailsSet")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "paymentLabels", source = "paymentLabels", qualifiedByName = "descriptionSet")
    @Mapping(target = "businessDocuments", source = "businessDocuments", qualifiedByName = "documentTitleSet")
    JobSheetDTO toDto(JobSheet s);

    @Mapping(target = "removeSignatories", ignore = true)
    @Mapping(target = "removeBusinessStamps", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removePaymentLabel", ignore = true)
    @Mapping(target = "removeBusinessDocument", ignore = true)
    JobSheet toEntity(JobSheetDTO jobSheetDTO);

    @Named("serialNumberSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "serialNumber", source = "serialNumber")
    Set<JobSheetDTO> toDtoSerialNumberSet(Set<JobSheet> jobSheet);
}
