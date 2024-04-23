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
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A MoratoriumItem.
 */
@Entity
@Table(name = "moratorium_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "moratoriumitem")
public class MoratoriumItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "moratorium_item_type_code", nullable = false, unique = true)
    private String moratoriumItemTypeCode;

    @NotNull
    @Column(name = "moratorium_item_type", nullable = false)
    private String moratoriumItemType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "moratorium_type_details")
    private String moratoriumTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MoratoriumItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMoratoriumItemTypeCode() {
        return this.moratoriumItemTypeCode;
    }

    public MoratoriumItem moratoriumItemTypeCode(String moratoriumItemTypeCode) {
        this.setMoratoriumItemTypeCode(moratoriumItemTypeCode);
        return this;
    }

    public void setMoratoriumItemTypeCode(String moratoriumItemTypeCode) {
        this.moratoriumItemTypeCode = moratoriumItemTypeCode;
    }

    public String getMoratoriumItemType() {
        return this.moratoriumItemType;
    }

    public MoratoriumItem moratoriumItemType(String moratoriumItemType) {
        this.setMoratoriumItemType(moratoriumItemType);
        return this;
    }

    public void setMoratoriumItemType(String moratoriumItemType) {
        this.moratoriumItemType = moratoriumItemType;
    }

    public String getMoratoriumTypeDetails() {
        return this.moratoriumTypeDetails;
    }

    public MoratoriumItem moratoriumTypeDetails(String moratoriumTypeDetails) {
        this.setMoratoriumTypeDetails(moratoriumTypeDetails);
        return this;
    }

    public void setMoratoriumTypeDetails(String moratoriumTypeDetails) {
        this.moratoriumTypeDetails = moratoriumTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoratoriumItem)) {
            return false;
        }
        return id != null && id.equals(((MoratoriumItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MoratoriumItem{" +
            "id=" + getId() +
            ", moratoriumItemTypeCode='" + getMoratoriumItemTypeCode() + "'" +
            ", moratoriumItemType='" + getMoratoriumItemType() + "'" +
            ", moratoriumTypeDetails='" + getMoratoriumTypeDetails() + "'" +
            "}";
    }
}
