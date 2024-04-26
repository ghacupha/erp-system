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
