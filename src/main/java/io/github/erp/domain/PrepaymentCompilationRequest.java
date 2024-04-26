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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.enumeration.CompilationStatusTypes;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
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

    @NotNull
    @Column(name = "compilation_token", nullable = false, unique = true)
    private UUID compilationToken;

    @ManyToMany
    @JoinTable(
        name = "rel_prepayment_compilation_request__placeholder",
        joinColumns = @JoinColumn(name = "prepayment_compilation_request_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

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

    public UUID getCompilationToken() {
        return this.compilationToken;
    }

    public PrepaymentCompilationRequest compilationToken(UUID compilationToken) {
        this.setCompilationToken(compilationToken);
        return this;
    }

    public void setCompilationToken(UUID compilationToken) {
        this.compilationToken = compilationToken;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public PrepaymentCompilationRequest placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public PrepaymentCompilationRequest addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public PrepaymentCompilationRequest removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
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
            ", compilationToken='" + getCompilationToken() + "'" +
            "}";
    }
}
