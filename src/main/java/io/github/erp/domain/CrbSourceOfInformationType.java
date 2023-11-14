package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CrbSourceOfInformationType.
 */
@Entity
@Table(name = "crb_source_of_information_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "crbsourceofinformationtype")
public class CrbSourceOfInformationType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "source_of_information_type_code", nullable = false, unique = true)
    private String sourceOfInformationTypeCode;

    @Column(name = "source_of_information_type_description", unique = true)
    private String sourceOfInformationTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CrbSourceOfInformationType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceOfInformationTypeCode() {
        return this.sourceOfInformationTypeCode;
    }

    public CrbSourceOfInformationType sourceOfInformationTypeCode(String sourceOfInformationTypeCode) {
        this.setSourceOfInformationTypeCode(sourceOfInformationTypeCode);
        return this;
    }

    public void setSourceOfInformationTypeCode(String sourceOfInformationTypeCode) {
        this.sourceOfInformationTypeCode = sourceOfInformationTypeCode;
    }

    public String getSourceOfInformationTypeDescription() {
        return this.sourceOfInformationTypeDescription;
    }

    public CrbSourceOfInformationType sourceOfInformationTypeDescription(String sourceOfInformationTypeDescription) {
        this.setSourceOfInformationTypeDescription(sourceOfInformationTypeDescription);
        return this;
    }

    public void setSourceOfInformationTypeDescription(String sourceOfInformationTypeDescription) {
        this.sourceOfInformationTypeDescription = sourceOfInformationTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbSourceOfInformationType)) {
            return false;
        }
        return id != null && id.equals(((CrbSourceOfInformationType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbSourceOfInformationType{" +
            "id=" + getId() +
            ", sourceOfInformationTypeCode='" + getSourceOfInformationTypeCode() + "'" +
            ", sourceOfInformationTypeDescription='" + getSourceOfInformationTypeDescription() + "'" +
            "}";
    }
}
