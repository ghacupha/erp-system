package io.github.erp.domain;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Mapped superclass for entities that hold UUID identifier data and also applicationUser
 * field, createdBy and lastModifiedBy
 */
@EntityListeners(ApplicationUserIdentifierEntityListener.class)
@MappedSuperclass
public abstract class AbstractIdentifiableEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "identifier")
    @JsonIgnore
    private UUID identifier = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "created_by_id", updatable = false)
    @JsonIgnore
    private ApplicationUser createdBy;

    @ManyToOne
    @JoinColumn(name = "last_modified_by_id")
    @JsonIgnore
    private ApplicationUser lastModifiedBy;

    @Column(name = "created_date", updatable = false)
    @JsonIgnore
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "last_modified_at")
    @JsonIgnore
    private ZonedDateTime lastModifiedAt = ZonedDateTime.now();

    public AbstractIdentifiableEntity() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public ApplicationUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ApplicationUser createdBy) {
        this.createdBy = createdBy;
    }

    public ApplicationUser getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(ApplicationUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(ZonedDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}
