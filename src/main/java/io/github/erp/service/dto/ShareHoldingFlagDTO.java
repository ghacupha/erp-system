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
import io.github.erp.domain.enumeration.ShareholdingFlagTypes;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.ShareHoldingFlag} entity.
 */
public class ShareHoldingFlagDTO implements Serializable {

    private Long id;

    @NotNull
    private ShareholdingFlagTypes shareholdingFlagTypeCode;

    @NotNull
    private String shareholdingFlagType;

    @Lob
    private String shareholdingTypeDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShareholdingFlagTypes getShareholdingFlagTypeCode() {
        return shareholdingFlagTypeCode;
    }

    public void setShareholdingFlagTypeCode(ShareholdingFlagTypes shareholdingFlagTypeCode) {
        this.shareholdingFlagTypeCode = shareholdingFlagTypeCode;
    }

    public String getShareholdingFlagType() {
        return shareholdingFlagType;
    }

    public void setShareholdingFlagType(String shareholdingFlagType) {
        this.shareholdingFlagType = shareholdingFlagType;
    }

    public String getShareholdingTypeDescription() {
        return shareholdingTypeDescription;
    }

    public void setShareholdingTypeDescription(String shareholdingTypeDescription) {
        this.shareholdingTypeDescription = shareholdingTypeDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShareHoldingFlagDTO)) {
            return false;
        }

        ShareHoldingFlagDTO shareHoldingFlagDTO = (ShareHoldingFlagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, shareHoldingFlagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShareHoldingFlagDTO{" +
            "id=" + getId() +
            ", shareholdingFlagTypeCode='" + getShareholdingFlagTypeCode() + "'" +
            ", shareholdingFlagType='" + getShareholdingFlagType() + "'" +
            ", shareholdingTypeDescription='" + getShareholdingTypeDescription() + "'" +
            "}";
    }
}
