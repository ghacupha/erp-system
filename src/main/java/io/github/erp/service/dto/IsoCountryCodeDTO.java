package io.github.erp.service.dto;

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
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.erp.domain.IsoCountryCode} entity.
 */
public class IsoCountryCodeDTO implements Serializable {

    private Long id;

    private String countryCode;

    private String countryDescription;

    private String continentCode;

    private String continentName;

    private String subRegion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryDescription() {
        return countryDescription;
    }

    public void setCountryDescription(String countryDescription) {
        this.countryDescription = countryDescription;
    }

    public String getContinentCode() {
        return continentCode;
    }

    public void setContinentCode(String continentCode) {
        this.continentCode = continentCode;
    }

    public String getContinentName() {
        return continentName;
    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    public String getSubRegion() {
        return subRegion;
    }

    public void setSubRegion(String subRegion) {
        this.subRegion = subRegion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IsoCountryCodeDTO)) {
            return false;
        }

        IsoCountryCodeDTO isoCountryCodeDTO = (IsoCountryCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, isoCountryCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IsoCountryCodeDTO{" +
            "id=" + getId() +
            ", countryCode='" + getCountryCode() + "'" +
            ", countryDescription='" + getCountryDescription() + "'" +
            ", continentCode='" + getContinentCode() + "'" +
            ", continentName='" + getContinentName() + "'" +
            ", subRegion='" + getSubRegion() + "'" +
            "}";
    }
}
