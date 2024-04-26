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
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.CountySubCountyCode} entity.
 */
public class CountySubCountyCodeDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 4, max = 4)
    @Pattern(regexp = "^\\d{4}$")
    private String subCountyCode;

    @NotNull
    private String subCountyName;

    @NotNull
    @Size(min = 2, max = 2)
    @Pattern(regexp = "^\\d{2}$")
    private String countyCode;

    @NotNull
    private String countyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubCountyCode() {
        return subCountyCode;
    }

    public void setSubCountyCode(String subCountyCode) {
        this.subCountyCode = subCountyCode;
    }

    public String getSubCountyName() {
        return subCountyName;
    }

    public void setSubCountyName(String subCountyName) {
        this.subCountyName = subCountyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountySubCountyCodeDTO)) {
            return false;
        }

        CountySubCountyCodeDTO countySubCountyCodeDTO = (CountySubCountyCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, countySubCountyCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountySubCountyCodeDTO{" +
            "id=" + getId() +
            ", subCountyCode='" + getSubCountyCode() + "'" +
            ", subCountyName='" + getSubCountyName() + "'" +
            ", countyCode='" + getCountyCode() + "'" +
            ", countyName='" + getCountyName() + "'" +
            "}";
    }
}
