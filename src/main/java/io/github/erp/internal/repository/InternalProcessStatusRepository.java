package io.github.erp.internal.repository;

/*-
 * Erp System - Mark II No 26 (Baruch Series)
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
