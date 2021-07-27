package io.github.erp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A MessageToken.
 */
@Entity
@Table(name = "message_token")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "messagetoken")
public class MessageToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "time_sent", nullable = false)
    private Long timeSent;

    @NotNull
    @Column(name = "token_value", nullable = false)
    private String tokenValue;

    @Column(name = "received")
    private Boolean received;

    @Column(name = "actioned")
    private Boolean actioned;

    @Column(name = "content_fully_enqueued")
    private Boolean contentFullyEnqueued;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public MessageToken description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTimeSent() {
        return timeSent;
    }

    public MessageToken timeSent(Long timeSent) {
        this.timeSent = timeSent;
        return this;
    }

    public void setTimeSent(Long timeSent) {
        this.timeSent = timeSent;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public MessageToken tokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
        return this;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public Boolean isReceived() {
        return received;
    }

    public MessageToken received(Boolean received) {
        this.received = received;
        return this;
    }

    public void setReceived(Boolean received) {
        this.received = received;
    }

    public Boolean isActioned() {
        return actioned;
    }

    public MessageToken actioned(Boolean actioned) {
        this.actioned = actioned;
        return this;
    }

    public void setActioned(Boolean actioned) {
        this.actioned = actioned;
    }

    public Boolean isContentFullyEnqueued() {
        return contentFullyEnqueued;
    }

    public MessageToken contentFullyEnqueued(Boolean contentFullyEnqueued) {
        this.contentFullyEnqueued = contentFullyEnqueued;
        return this;
    }

    public void setContentFullyEnqueued(Boolean contentFullyEnqueued) {
        this.contentFullyEnqueued = contentFullyEnqueued;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageToken)) {
            return false;
        }
        return id != null && id.equals(((MessageToken) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageToken{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", timeSent=" + getTimeSent() +
            ", tokenValue='" + getTokenValue() + "'" +
            ", received='" + isReceived() + "'" +
            ", actioned='" + isActioned() + "'" +
            ", contentFullyEnqueued='" + isContentFullyEnqueued() + "'" +
            "}";
    }
}
