package io.github.erp.service.mapper;

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

    @Named("serialNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "serialNumber", source = "serialNumber")
    JobSheetDTO toDtoSerialNumber(JobSheet jobSheet);
}
