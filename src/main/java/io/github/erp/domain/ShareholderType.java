package io.github.erp.domain;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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

import io.github.erp.domain.enumeration.ShareHolderTypes;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ShareholderType.
 */
@Entity
@Table(name = "shareholder_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "shareholdertype")
public class ShareholderType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "share_holder_type_code", nullable = false, unique = true)
    private String shareHolderTypeCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "share_holder_type", nullable = false)
    private ShareHolderTypes shareHolderType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShareholderType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShareHolderTypeCode() {
        return this.shareHolderTypeCode;
    }

    public ShareholderType shareHolderTypeCode(String shareHolderTypeCode) {
        this.setShareHolderTypeCode(shareHolderTypeCode);
        return this;
    }

    public void setShareHolderTypeCode(String shareHolderTypeCode) {
        this.shareHolderTypeCode = shareHolderTypeCode;
    }

    public ShareHolderTypes getShareHolderType() {
        return this.shareHolderType;
    }

    public ShareholderType shareHolderType(ShareHolderTypes shareHolderType) {
        this.setShareHolderType(shareHolderType);
        return this;
    }

    public void setShareHolderType(ShareHolderTypes shareHolderType) {
        this.shareHolderType = shareHolderType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShareholderType)) {
            return false;
        }
        return id != null && id.equals(((ShareholderType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShareholderType{" +
            "id=" + getId() +
            ", shareHolderTypeCode='" + getShareHolderTypeCode() + "'" +
            ", shareHolderType='" + getShareHolderType() + "'" +
            "}";
    }
}
