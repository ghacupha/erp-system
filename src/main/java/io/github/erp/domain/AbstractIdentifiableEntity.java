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
