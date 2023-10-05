package io.github.erp.domain;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
