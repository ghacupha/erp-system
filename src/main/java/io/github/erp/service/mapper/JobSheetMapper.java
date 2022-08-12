package io.github.erp.service.mapper;

/*-
 * Erp System - Mark II No 26 (Baruch Series) Server ver 0.0.6-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.JobSheet;
import io.github.erp.service.dto.JobSheetDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link JobSheet} and its DTO {@link JobSheetDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { DealerMapper.class, BusinessStampMapper.class, PlaceholderMapper.class, PaymentLabelMapper.class }
)
public interface JobSheetMapper extends EntityMapper<JobSheetDTO, JobSheet> {
    @Mapping(target = "biller", source = "biller", qualifiedByName = "dealerName")
    @Mapping(target = "signatories", source = "signatories", qualifiedByName = "dealerNameSet")
    @Mapping(target = "contactPerson", source = "contactPerson", qualifiedByName = "dealerName")
    @Mapping(target = "businessStamps", source = "businessStamps", qualifiedByName = "detailsSet")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "paymentLabels", source = "paymentLabels", qualifiedByName = "descriptionSet")
    JobSheetDTO toDto(JobSheet s);

    @Mapping(target = "removeSignatories", ignore = true)
    @Mapping(target = "removeBusinessStamps", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    @Mapping(target = "removePaymentLabel", ignore = true)
    JobSheet toEntity(JobSheetDTO jobSheetDTO);

    @Named("serialNumberSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "serialNumber", source = "serialNumber")
    Set<JobSheetDTO> toDtoSerialNumberSet(Set<JobSheet> jobSheet);
}
