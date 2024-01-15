package io.github.erp.domain;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.internal.service.applicationUser.CurrentUserContext;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class ApplicationUserIdentifierEntityListener {

    @PrePersist
    public void prePersist(AbstractIdentifiableEntity entity) {

        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        entity.setCreatedAt(now);
        entity.setLastModifiedAt(now);

        ApplicationUser currentUser = getCurrentUser();

        if (currentUser != null) {
            entity.setCreatedBy(currentUser);
            entity.setLastModifiedBy(currentUser);
        }
    }

    @PreUpdate
    public void preUpdate(AbstractIdentifiableEntity entity) {

        entity.setLastModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));

        ApplicationUser currentUser = getCurrentUser();
        if (currentUser != null) {
            entity.setLastModifiedBy(currentUser);
        }
    }

    private ApplicationUser getCurrentUser() {
        return CurrentUserContext.getCurrentUser();
    }
}

