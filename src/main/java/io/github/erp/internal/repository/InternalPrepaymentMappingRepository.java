package io.github.erp.internal.repository;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.4
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import java.util.Optional;

import io.github.erp.repository.PrepaymentMappingRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import io.github.erp.domain.PrepaymentMapping;

/**
 * Custom repo specifically for preppayment-mapping
 */
@Repository
public interface InternalPrepaymentMappingRepository extends
    PrepaymentMappingRepository,
    JpaRepository<PrepaymentMapping, Long>,
    JpaSpecificationExecutor<PrepaymentMapping> {

        Optional<PrepaymentMapping> findByParameterKeyEquals(String parameterKey);

}
