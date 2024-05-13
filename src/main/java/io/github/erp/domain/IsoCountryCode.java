package io.github.erp.domain;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IsoCountryCode.
 */
@Entity
@Table(name = "iso_country_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "isocountrycode")
public class IsoCountryCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "country_description")
    private String countryDescription;

    @Column(name = "continent_code")
    private String continentCode;

    @Column(name = "continent_name")
    private String continentName;

    @Column(name = "sub_region")
    private String subRegion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IsoCountryCode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public IsoCountryCode countryCode(String countryCode) {
        this.setCountryCode(countryCode);
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryDescription() {
        return this.countryDescription;
    }

    public IsoCountryCode countryDescription(String countryDescription) {
        this.setCountryDescription(countryDescription);
        return this;
    }

    public void setCountryDescription(String countryDescription) {
        this.countryDescription = countryDescription;
    }

    public String getContinentCode() {
        return this.continentCode;
    }

    public IsoCountryCode continentCode(String continentCode) {
        this.setContinentCode(continentCode);
        return this;
    }

    public void setContinentCode(String continentCode) {
        this.continentCode = continentCode;
    }

    public String getContinentName() {
        return this.continentName;
    }

    public IsoCountryCode continentName(String continentName) {
        this.setContinentName(continentName);
        return this;
    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    public String getSubRegion() {
        return this.subRegion;
    }

    public IsoCountryCode subRegion(String subRegion) {
        this.setSubRegion(subRegion);
        return this;
    }

    public void setSubRegion(String subRegion) {
        this.subRegion = subRegion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IsoCountryCode)) {
            return false;
        }
        return id != null && id.equals(((IsoCountryCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IsoCountryCode{" +
            "id=" + getId() +
            ", countryCode='" + getCountryCode() + "'" +
            ", countryDescription='" + getCountryDescription() + "'" +
            ", continentCode='" + getContinentCode() + "'" +
            ", continentName='" + getContinentName() + "'" +
            ", subRegion='" + getSubRegion() + "'" +
            "}";
    }
}
