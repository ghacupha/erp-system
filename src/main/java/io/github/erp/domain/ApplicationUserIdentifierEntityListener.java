package io.github.erp.domain;

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
        // Update modifiedAt on entity update
        entity.setLastModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));

        ApplicationUser currentUser = getCurrentUser(); // Get the current user
        if (currentUser != null) {
            entity.setLastModifiedBy(currentUser);
        }
    }

    private ApplicationUser getCurrentUser() {
        return CurrentUserContext.getCurrentUser();
    }
}

