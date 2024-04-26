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
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OutletType.
 */
@Entity
@Table(name = "outlet_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "outlettype")
public class OutletType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "outlet_type_code", nullable = false, unique = true)
    private String outletTypeCode;

    @NotNull
    @Column(name = "outlet_type", nullable = false, unique = true)
    private String outletType;

    @Column(name = "outlet_type_details")
    private String outletTypeDetails;

    @ManyToMany
    @JoinTable(
        name = "rel_outlet_type__placeholder",
        joinColumns = @JoinColumn(name = "outlet_type_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OutletType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutletTypeCode() {
        return this.outletTypeCode;
    }

    public OutletType outletTypeCode(String outletTypeCode) {
        this.setOutletTypeCode(outletTypeCode);
        return this;
    }

    public void setOutletTypeCode(String outletTypeCode) {
        this.outletTypeCode = outletTypeCode;
    }

    public String getOutletType() {
        return this.outletType;
    }

    public OutletType outletType(String outletType) {
        this.setOutletType(outletType);
        return this;
    }

    public void setOutletType(String outletType) {
        this.outletType = outletType;
    }

    public String getOutletTypeDetails() {
        return this.outletTypeDetails;
    }

    public OutletType outletTypeDetails(String outletTypeDetails) {
        this.setOutletTypeDetails(outletTypeDetails);
        return this;
    }

    public void setOutletTypeDetails(String outletTypeDetails) {
        this.outletTypeDetails = outletTypeDetails;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public OutletType placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public OutletType addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public OutletType removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OutletType)) {
            return false;
        }
        return id != null && id.equals(((OutletType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutletType{" +
            "id=" + getId() +
            ", outletTypeCode='" + getOutletTypeCode() + "'" +
            ", outletType='" + getOutletType() + "'" +
            ", outletTypeDetails='" + getOutletTypeDetails() + "'" +
            "}";
    }
}
