package io.github.erp.service.mapper;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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


import io.github.erp.domain.*;
import io.github.erp.service.dto.PaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = {PaymentCalculationMapper.class, PaymentRequisitionMapper.class, TaxRuleMapper.class})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {

    @Mapping(source = "paymentCalculation.id", target = "paymentCalculationId")
    @Mapping(source = "paymentRequisition.id", target = "paymentRequisitionId")
    @Mapping(source = "taxRule.id", target = "taxRuleId")
    PaymentDTO toDto(Payment payment);

    @Mapping(target = "ownedInvoices", ignore = true)
    @Mapping(target = "removeOwnedInvoice", ignore = true)
    @Mapping(source = "paymentCalculationId", target = "paymentCalculation")
    @Mapping(source = "paymentRequisitionId", target = "paymentRequisition")
    @Mapping(target = "dealers", ignore = true)
    @Mapping(target = "removeDealer", ignore = true)
    @Mapping(source = "taxRuleId", target = "taxRule")
    Payment toEntity(PaymentDTO paymentDTO);

    default Payment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setId(id);
        return payment;
    }
}
