package io.github.erp.internal.repository;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.User;
import io.github.erp.repository.ApplicationUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InternalApplicationUserRepository
    extends
    ApplicationUserRepository,
    JpaRepository<ApplicationUser, Long>,
    JpaSpecificationExecutor<ApplicationUser>{

    @Query("SELECT " +
        "au " +
        "FROM ApplicationUser au " +
        "LEFT JOIN FETCH au.systemIdentity u " +
        "LEFT JOIN FETCH au.department " +
        "LEFT JOIN FETCH au.dealerIdentity di " +
        "LEFT JOIN FETCH di.paymentLabels " +
        "LEFT JOIN FETCH au.organization " +
        "LEFT JOIN FETCH au.placeholders " +
        "LEFT JOIN FETCH au.securityClearance " +
        "LEFT JOIN FETCH au.userProperties " +
        "WHERE au.systemIdentity = :user")
    Optional<ApplicationUser> findApplicationUserBySystemIdentity(@Param("user") User systemIdentity);
}
