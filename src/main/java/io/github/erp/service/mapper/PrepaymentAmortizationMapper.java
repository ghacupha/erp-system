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
import io.github.erp.domain.PrepaymentAmortization;
import io.github.erp.service.dto.PrepaymentAmortizationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrepaymentAmortization} and its DTO {@link PrepaymentAmortizationDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        PrepaymentAccountMapper.class,
        SettlementCurrencyMapper.class,
        TransactionAccountMapper.class,
        PlaceholderMapper.class,
        FiscalMonthMapper.class,
        PrepaymentCompilationRequestMapper.class,
    }
)
public interface PrepaymentAmortizationMapper extends EntityMapper<PrepaymentAmortizationDTO, PrepaymentAmortization> {
    @Mapping(target = "prepaymentAccount", source = "prepaymentAccount", qualifiedByName = "catalogueNumber")
    @Mapping(target = "settlementCurrency", source = "settlementCurrency", qualifiedByName = "iso4217CurrencyCode")
    @Mapping(target = "debitAccount", source = "debitAccount", qualifiedByName = "accountNumber")
    @Mapping(target = "creditAccount", source = "creditAccount", qualifiedByName = "accountNumber")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "fiscalMonth", source = "fiscalMonth", qualifiedByName = "endDate")
    @Mapping(target = "prepaymentCompilationRequest", source = "prepaymentCompilationRequest", qualifiedByName = "id")
    PrepaymentAmortizationDTO toDto(PrepaymentAmortization s);

    @Mapping(target = "removePlaceholder", ignore = true)
    PrepaymentAmortization toEntity(PrepaymentAmortizationDTO prepaymentAmortizationDTO);
}
