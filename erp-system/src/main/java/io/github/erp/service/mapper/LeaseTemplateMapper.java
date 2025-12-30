package io.github.erp.service.mapper;
/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.LeaseTemplate;
import io.github.erp.service.dto.LeaseTemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeaseTemplate} and its DTO {@link LeaseTemplateDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { TransactionAccountMapper.class, AssetCategoryMapper.class, ServiceOutletMapper.class, DealerMapper.class }
)
public interface LeaseTemplateMapper extends EntityMapper<LeaseTemplateDTO, LeaseTemplate> {
    @Mapping(target = "assetAccount", source = "assetAccount", qualifiedByName = "accountName")
    @Mapping(target = "depreciationAccount", source = "depreciationAccount", qualifiedByName = "accountName")
    @Mapping(target = "accruedDepreciationAccount", source = "accruedDepreciationAccount", qualifiedByName = "accountName")
    @Mapping(target = "interestPaidTransferDebitAccount", source = "interestPaidTransferDebitAccount", qualifiedByName = "accountName")
    @Mapping(target = "interestPaidTransferCreditAccount", source = "interestPaidTransferCreditAccount", qualifiedByName = "accountName")
    @Mapping(target = "interestAccruedDebitAccount", source = "interestAccruedDebitAccount", qualifiedByName = "accountName")
    @Mapping(target = "interestAccruedCreditAccount", source = "interestAccruedCreditAccount", qualifiedByName = "accountName")
    @Mapping(target = "leaseRecognitionDebitAccount", source = "leaseRecognitionDebitAccount", qualifiedByName = "accountName")
    @Mapping(target = "leaseRecognitionCreditAccount", source = "leaseRecognitionCreditAccount", qualifiedByName = "accountName")
    @Mapping(target = "leaseRepaymentDebitAccount", source = "leaseRepaymentDebitAccount", qualifiedByName = "accountName")
    @Mapping(target = "leaseRepaymentCreditAccount", source = "leaseRepaymentCreditAccount", qualifiedByName = "accountName")
    @Mapping(target = "rouRecognitionCreditAccount", source = "rouRecognitionCreditAccount", qualifiedByName = "accountName")
    @Mapping(target = "rouRecognitionDebitAccount", source = "rouRecognitionDebitAccount", qualifiedByName = "accountName")
    @Mapping(target = "assetCategory", source = "assetCategory", qualifiedByName = "assetCategoryName")
    @Mapping(target = "serviceOutlet", source = "serviceOutlet", qualifiedByName = "outletCode")
    @Mapping(target = "mainDealer", source = "mainDealer", qualifiedByName = "dealerName")
    LeaseTemplateDTO toDto(LeaseTemplate s);

    @Named("templateTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "templateTitle", source = "templateTitle")
    LeaseTemplateDTO toDtoTemplateTitle(LeaseTemplate leaseTemplate);
}
