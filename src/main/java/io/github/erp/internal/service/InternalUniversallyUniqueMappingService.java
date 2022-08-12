package io.github.erp.internal.service;

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
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.service.UniversallyUniqueMappingService;

import java.util.Optional;

/**
 * This service extends the functionality of the common service to simply select values that
 * are mapped to a particular key and are called in various parts of the programming for instance
 * to search out common values or preferred inputs. This has helped us avoid hard-coding business
 * logic into the app
 */
public interface InternalUniversallyUniqueMappingService extends UniversallyUniqueMappingService {

    /**
     * Search for the universallyUniqueMapping corresponding to the query.
     *
     * @param universalKey the query of the search.
     *
     * @return the value mapped to that key
     */
    Optional<UniversallyUniqueMapping> getMapping(String universalKey);
}
