package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.MessageToken} entity. This class is used
 * in {@link io.github.erp.web.rest.MessageTokenResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /message-tokens?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MessageTokenCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private LongFilter timeSent;

    private StringFilter tokenValue;

    private BooleanFilter received;

    private BooleanFilter actioned;

    private BooleanFilter contentFullyEnqueued;

    public MessageTokenCriteria() {
    }

    public MessageTokenCriteria(MessageTokenCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.timeSent = other.timeSent == null ? null : other.timeSent.copy();
        this.tokenValue = other.tokenValue == null ? null : other.tokenValue.copy();
        this.received = other.received == null ? null : other.received.copy();
        this.actioned = other.actioned == null ? null : other.actioned.copy();
        this.contentFullyEnqueued = other.contentFullyEnqueued == null ? null : other.contentFullyEnqueued.copy();
    }

    @Override
    public MessageTokenCriteria copy() {
        return new MessageTokenCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LongFilter timeSent) {
        this.timeSent = timeSent;
    }

    public StringFilter getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(StringFilter tokenValue) {
        this.tokenValue = tokenValue;
    }

    public BooleanFilter getReceived() {
        return received;
    }

    public void setReceived(BooleanFilter received) {
        this.received = received;
    }

    public BooleanFilter getActioned() {
        return actioned;
    }

    public void setActioned(BooleanFilter actioned) {
        this.actioned = actioned;
    }

    public BooleanFilter getContentFullyEnqueued() {
        return contentFullyEnqueued;
    }

    public void setContentFullyEnqueued(BooleanFilter contentFullyEnqueued) {
        this.contentFullyEnqueued = contentFullyEnqueued;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MessageTokenCriteria that = (MessageTokenCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(timeSent, that.timeSent) &&
            Objects.equals(tokenValue, that.tokenValue) &&
            Objects.equals(received, that.received) &&
            Objects.equals(actioned, that.actioned) &&
            Objects.equals(contentFullyEnqueued, that.contentFullyEnqueued);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        description,
        timeSent,
        tokenValue,
        received,
        actioned,
        contentFullyEnqueued
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageTokenCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (timeSent != null ? "timeSent=" + timeSent + ", " : "") +
                (tokenValue != null ? "tokenValue=" + tokenValue + ", " : "") +
                (received != null ? "received=" + received + ", " : "") +
                (actioned != null ? "actioned=" + actioned + ", " : "") +
                (contentFullyEnqueued != null ? "contentFullyEnqueued=" + contentFullyEnqueued + ", " : "") +
            "}";
    }

}
