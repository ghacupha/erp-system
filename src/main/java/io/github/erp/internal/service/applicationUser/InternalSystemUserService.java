
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
package io.github.erp.internal.service.applicationUser;

import io.github.erp.domain.User;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * This is a service to bring common implementations of entities to the system generated
 * User entity, among them and probably most important the search
 */
public interface InternalSystemUserService {

    /**
     * Search for the User corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @return the list of entities.
     */
    List<UserDTO> search(String query);

    /**
     *
     * @return List of users from the system generated User entity
     */
    Optional<List<User>> findAll();
}
