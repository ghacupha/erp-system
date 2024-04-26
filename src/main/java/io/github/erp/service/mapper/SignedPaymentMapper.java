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
import io.github.erp.domain.SignedPayment;
import io.github.erp.service.dto.SignedPaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SignedPayment} and its DTO {@link SignedPaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = { PaymentLabelMapper.class, PaymentCategoryMapper.class, PlaceholderMapper.class })
public interface SignedPaymentMapper extends EntityMapper<SignedPaymentDTO, SignedPayment> {
    @Mapping(target = "paymentLabels", source = "paymentLabels", qualifiedByName = "descriptionSet")
    @Mapping(target = "paymentCategory", source = "paymentCategory", qualifiedByName = "categoryName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    @Mapping(target = "signedPaymentGroup", source = "signedPaymentGroup", qualifiedByName = "id")
    SignedPaymentDTO toDto(SignedPayment s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SignedPaymentDTO toDtoId(SignedPayment signedPayment);

    @Mapping(target = "removePaymentLabel", ignore = true)
    @Mapping(target = "removePlaceholder", ignore = true)
    SignedPayment toEntity(SignedPaymentDTO signedPaymentDTO);
}
