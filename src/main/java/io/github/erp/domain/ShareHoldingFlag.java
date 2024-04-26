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
import io.github.erp.domain.enumeration.ShareholdingFlagTypes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A ShareHoldingFlag.
 */
@Entity
@Table(name = "share_holding_flag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "shareholdingflag")
public class ShareHoldingFlag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "shareholding_flag_type_code", nullable = false)
    private ShareholdingFlagTypes shareholdingFlagTypeCode;

    @NotNull
    @Column(name = "shareholding_flag_type", nullable = false, unique = true)
    private String shareholdingFlagType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "shareholding_type_description")
    private String shareholdingTypeDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShareHoldingFlag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShareholdingFlagTypes getShareholdingFlagTypeCode() {
        return this.shareholdingFlagTypeCode;
    }

    public ShareHoldingFlag shareholdingFlagTypeCode(ShareholdingFlagTypes shareholdingFlagTypeCode) {
        this.setShareholdingFlagTypeCode(shareholdingFlagTypeCode);
        return this;
    }

    public void setShareholdingFlagTypeCode(ShareholdingFlagTypes shareholdingFlagTypeCode) {
        this.shareholdingFlagTypeCode = shareholdingFlagTypeCode;
    }

    public String getShareholdingFlagType() {
        return this.shareholdingFlagType;
    }

    public ShareHoldingFlag shareholdingFlagType(String shareholdingFlagType) {
        this.setShareholdingFlagType(shareholdingFlagType);
        return this;
    }

    public void setShareholdingFlagType(String shareholdingFlagType) {
        this.shareholdingFlagType = shareholdingFlagType;
    }

    public String getShareholdingTypeDescription() {
        return this.shareholdingTypeDescription;
    }

    public ShareHoldingFlag shareholdingTypeDescription(String shareholdingTypeDescription) {
        this.setShareholdingTypeDescription(shareholdingTypeDescription);
        return this;
    }

    public void setShareholdingTypeDescription(String shareholdingTypeDescription) {
        this.shareholdingTypeDescription = shareholdingTypeDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShareHoldingFlag)) {
            return false;
        }
        return id != null && id.equals(((ShareHoldingFlag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShareHoldingFlag{" +
            "id=" + getId() +
            ", shareholdingFlagTypeCode='" + getShareholdingFlagTypeCode() + "'" +
            ", shareholdingFlagType='" + getShareholdingFlagType() + "'" +
            ", shareholdingTypeDescription='" + getShareholdingTypeDescription() + "'" +
            "}";
    }
}
