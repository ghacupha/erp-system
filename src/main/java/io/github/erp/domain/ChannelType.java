package io.github.erp.domain;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
 * A ChannelType.
 */
@Entity
@Table(name = "channel_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "channeltype")
public class ChannelType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "channels_type_code", nullable = false, unique = true)
    private String channelsTypeCode;

    @NotNull
    @Column(name = "channel_types", nullable = false, unique = true)
    private String channelTypes;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "channel_type_details")
    private String channelTypeDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChannelType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelsTypeCode() {
        return this.channelsTypeCode;
    }

    public ChannelType channelsTypeCode(String channelsTypeCode) {
        this.setChannelsTypeCode(channelsTypeCode);
        return this;
    }

    public void setChannelsTypeCode(String channelsTypeCode) {
        this.channelsTypeCode = channelsTypeCode;
    }

    public String getChannelTypes() {
        return this.channelTypes;
    }

    public ChannelType channelTypes(String channelTypes) {
        this.setChannelTypes(channelTypes);
        return this;
    }

    public void setChannelTypes(String channelTypes) {
        this.channelTypes = channelTypes;
    }

    public String getChannelTypeDetails() {
        return this.channelTypeDetails;
    }

    public ChannelType channelTypeDetails(String channelTypeDetails) {
        this.setChannelTypeDetails(channelTypeDetails);
        return this;
    }

    public void setChannelTypeDetails(String channelTypeDetails) {
        this.channelTypeDetails = channelTypeDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChannelType)) {
            return false;
        }
        return id != null && id.equals(((ChannelType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChannelType{" +
            "id=" + getId() +
            ", channelsTypeCode='" + getChannelsTypeCode() + "'" +
            ", channelTypes='" + getChannelTypes() + "'" +
            ", channelTypeDetails='" + getChannelTypeDetails() + "'" +
            "}";
    }
}
