package io.github.erp.internal.repository;

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
import io.github.erp.domain.PrepaymentCompilationRequest;
import io.github.erp.repository.PrepaymentAmortizationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface InternalPrepaymentAmortizationRepository extends
    PrepaymentAmortizationRepository,
    JpaRepository<PrepaymentAmortization, Long>,
    JpaSpecificationExecutor<PrepaymentAmortization> {

    List<PrepaymentAmortization> findAllByPrepaymentCompilationRequest(@NotNull PrepaymentCompilationRequest prepaymentCompilationRequest);
}
