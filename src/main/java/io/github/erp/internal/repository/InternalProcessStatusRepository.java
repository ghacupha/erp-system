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
import io.github.erp.domain.ProcessStatus;
import io.github.erp.repository.ProcessStatusRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InternalProcessStatusRepository extends ProcessStatusRepository, JpaRepository<ProcessStatus, Long>, JpaSpecificationExecutor<ProcessStatus> {

    /**
     * This query returns the status where the description is equivalent to the description provided
     *
     * @param desc
     * @return
     */
    Optional<ProcessStatus> findProcessStatusByDescriptionEquals(String desc);
}
