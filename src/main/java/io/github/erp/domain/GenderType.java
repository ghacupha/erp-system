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
import io.github.erp.domain.enumeration.genderTypes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A GenderType.
 */
@Entity
@Table(name = "gender_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "gendertype")
public class GenderType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "gender_code", nullable = false, unique = true)
    private String genderCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender_type", nullable = false, unique = true)
    private genderTypes genderType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "gender_description")
    private String genderDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GenderType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenderCode() {
        return this.genderCode;
    }

    public GenderType genderCode(String genderCode) {
        this.setGenderCode(genderCode);
        return this;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public genderTypes getGenderType() {
        return this.genderType;
    }

    public GenderType genderType(genderTypes genderType) {
        this.setGenderType(genderType);
        return this;
    }

    public void setGenderType(genderTypes genderType) {
        this.genderType = genderType;
    }

    public String getGenderDescription() {
        return this.genderDescription;
    }

    public GenderType genderDescription(String genderDescription) {
        this.setGenderDescription(genderDescription);
        return this;
    }

    public void setGenderDescription(String genderDescription) {
        this.genderDescription = genderDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenderType)) {
            return false;
        }
        return id != null && id.equals(((GenderType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenderType{" +
            "id=" + getId() +
            ", genderCode='" + getGenderCode() + "'" +
            ", genderType='" + getGenderType() + "'" +
            ", genderDescription='" + getGenderDescription() + "'" +
            "}";
    }
}
