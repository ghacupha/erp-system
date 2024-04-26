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
 * A DTO for the {@link io.github.erp.domain.CrbAgingBands} entity.
 */
public class CrbAgingBandsDTO implements Serializable {

    private Long id;

    @NotNull
    private String agingBandCategoryCode;

    @NotNull
    private String agingBandCategory;

    @NotNull
    private String agingBandCategoryDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgingBandCategoryCode() {
        return agingBandCategoryCode;
    }

    public void setAgingBandCategoryCode(String agingBandCategoryCode) {
        this.agingBandCategoryCode = agingBandCategoryCode;
    }

    public String getAgingBandCategory() {
        return agingBandCategory;
    }

    public void setAgingBandCategory(String agingBandCategory) {
        this.agingBandCategory = agingBandCategory;
    }

    public String getAgingBandCategoryDetails() {
        return agingBandCategoryDetails;
    }

    public void setAgingBandCategoryDetails(String agingBandCategoryDetails) {
        this.agingBandCategoryDetails = agingBandCategoryDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrbAgingBandsDTO)) {
            return false;
        }

        CrbAgingBandsDTO crbAgingBandsDTO = (CrbAgingBandsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, crbAgingBandsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrbAgingBandsDTO{" +
            "id=" + getId() +
            ", agingBandCategoryCode='" + getAgingBandCategoryCode() + "'" +
            ", agingBandCategory='" + getAgingBandCategory() + "'" +
            ", agingBandCategoryDetails='" + getAgingBandCategoryDetails() + "'" +
            "}";
    }
}
