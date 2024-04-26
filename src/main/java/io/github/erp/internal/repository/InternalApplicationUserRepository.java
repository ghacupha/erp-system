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
