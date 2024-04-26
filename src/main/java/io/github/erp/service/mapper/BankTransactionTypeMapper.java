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
import io.github.erp.domain.BankTransactionType;
import io.github.erp.service.dto.BankTransactionTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BankTransactionType} and its DTO {@link BankTransactionTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BankTransactionTypeMapper extends EntityMapper<BankTransactionTypeDTO, BankTransactionType> {
    @Named("transactionTypeCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "transactionTypeCode", source = "transactionTypeCode")
    BankTransactionTypeDTO toDtoTransactionTypeCode(BankTransactionType bankTransactionType);

    @Named("transactionTypeDetails")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "transactionTypeDetails", source = "transactionTypeDetails")
    BankTransactionTypeDTO toDtoTransactionTypeDetails(BankTransactionType bankTransactionType);
}
