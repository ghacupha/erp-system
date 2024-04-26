package io.github.erp.domain;

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

