package io.github.erp.domain;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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

import io.github.erp.domain.enumeration.CompilationStatusTypes;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PrepaymentCompilationRequest.
 */
@Entity
@Table(name = "prepayment_compilation_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "prepaymentcompilationrequest")
public class PrepaymentCompilationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "time_of_request")
    private ZonedDateTime timeOfRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "compilation_status")
    private CompilationStatusTypes compilationStatus;

    @Column(name = "items_processed")
    private Integer itemsProcessed;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PrepaymentCompilationRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTimeOfRequest() {
        return this.timeOfRequest;
    }

    public PrepaymentCompilationRequest timeOfRequest(ZonedDateTime timeOfRequest) {
        this.setTimeOfRequest(timeOfRequest);
        return this;
    }

    public void setTimeOfRequest(ZonedDateTime timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public CompilationStatusTypes getCompilationStatus() {
        return this.compilationStatus;
    }

    public PrepaymentCompilationRequest compilationStatus(CompilationStatusTypes compilationStatus) {
        this.setCompilationStatus(compilationStatus);
        return this;
    }

    public void setCompilationStatus(CompilationStatusTypes compilationStatus) {
        this.compilationStatus = compilationStatus;
    }

    public Integer getItemsProcessed() {
        return this.itemsProcessed;
    }

    public PrepaymentCompilationRequest itemsProcessed(Integer itemsProcessed) {
        this.setItemsProcessed(itemsProcessed);
        return this;
    }

    public void setItemsProcessed(Integer itemsProcessed) {
        this.itemsProcessed = itemsProcessed;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrepaymentCompilationRequest)) {
            return false;
        }
        return id != null && id.equals(((PrepaymentCompilationRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentCompilationRequest{" +
            "id=" + getId() +
            ", timeOfRequest='" + getTimeOfRequest() + "'" +
            ", compilationStatus='" + getCompilationStatus() + "'" +
            ", itemsProcessed=" + getItemsProcessed() +
            "}";
    }
}
