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

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SnaSectorCode.
 */
@Entity
@Table(name = "sna_sector_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "snasectorcode")
public class SnaSectorCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "sector_type_code", nullable = false, unique = true)
    private String sectorTypeCode;

    @Column(name = "main_sector_code")
    private String mainSectorCode;

    @Column(name = "main_sector_type_name")
    private String mainSectorTypeName;

    @Column(name = "sub_sector_code")
    private String subSectorCode;

    @Column(name = "sub_sector_name")
    private String subSectorName;

    @Column(name = "sub_sub_sector_code")
    private String subSubSectorCode;

    @Column(name = "sub_sub_sector_name")
    private String subSubSectorName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SnaSectorCode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectorTypeCode() {
        return this.sectorTypeCode;
    }

    public SnaSectorCode sectorTypeCode(String sectorTypeCode) {
        this.setSectorTypeCode(sectorTypeCode);
        return this;
    }

    public void setSectorTypeCode(String sectorTypeCode) {
        this.sectorTypeCode = sectorTypeCode;
    }

    public String getMainSectorCode() {
        return this.mainSectorCode;
    }

    public SnaSectorCode mainSectorCode(String mainSectorCode) {
        this.setMainSectorCode(mainSectorCode);
        return this;
    }

    public void setMainSectorCode(String mainSectorCode) {
        this.mainSectorCode = mainSectorCode;
    }

    public String getMainSectorTypeName() {
        return this.mainSectorTypeName;
    }

    public SnaSectorCode mainSectorTypeName(String mainSectorTypeName) {
        this.setMainSectorTypeName(mainSectorTypeName);
        return this;
    }

    public void setMainSectorTypeName(String mainSectorTypeName) {
        this.mainSectorTypeName = mainSectorTypeName;
    }

    public String getSubSectorCode() {
        return this.subSectorCode;
    }

    public SnaSectorCode subSectorCode(String subSectorCode) {
        this.setSubSectorCode(subSectorCode);
        return this;
    }

    public void setSubSectorCode(String subSectorCode) {
        this.subSectorCode = subSectorCode;
    }

    public String getSubSectorName() {
        return this.subSectorName;
    }

    public SnaSectorCode subSectorName(String subSectorName) {
        this.setSubSectorName(subSectorName);
        return this;
    }

    public void setSubSectorName(String subSectorName) {
        this.subSectorName = subSectorName;
    }

    public String getSubSubSectorCode() {
        return this.subSubSectorCode;
    }

    public SnaSectorCode subSubSectorCode(String subSubSectorCode) {
        this.setSubSubSectorCode(subSubSectorCode);
        return this;
    }

    public void setSubSubSectorCode(String subSubSectorCode) {
        this.subSubSectorCode = subSubSectorCode;
    }

    public String getSubSubSectorName() {
        return this.subSubSectorName;
    }

    public SnaSectorCode subSubSectorName(String subSubSectorName) {
        this.setSubSubSectorName(subSubSectorName);
        return this;
    }

    public void setSubSubSectorName(String subSubSectorName) {
        this.subSubSectorName = subSubSectorName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SnaSectorCode)) {
            return false;
        }
        return id != null && id.equals(((SnaSectorCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SnaSectorCode{" +
            "id=" + getId() +
            ", sectorTypeCode='" + getSectorTypeCode() + "'" +
            ", mainSectorCode='" + getMainSectorCode() + "'" +
            ", mainSectorTypeName='" + getMainSectorTypeName() + "'" +
            ", subSectorCode='" + getSubSectorCode() + "'" +
            ", subSectorName='" + getSubSectorName() + "'" +
            ", subSubSectorCode='" + getSubSubSectorCode() + "'" +
            ", subSubSectorName='" + getSubSubSectorName() + "'" +
            "}";
    }
}
