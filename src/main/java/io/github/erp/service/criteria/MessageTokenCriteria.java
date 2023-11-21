package io.github.erp.service.criteria;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

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

    private LongFilter placeholderId;

    private Boolean distinct;

    public MessageTokenCriteria() {}

    public MessageTokenCriteria(MessageTokenCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.timeSent = other.timeSent == null ? null : other.timeSent.copy();
        this.tokenValue = other.tokenValue == null ? null : other.tokenValue.copy();
        this.received = other.received == null ? null : other.received.copy();
        this.actioned = other.actioned == null ? null : other.actioned.copy();
        this.contentFullyEnqueued = other.contentFullyEnqueued == null ? null : other.contentFullyEnqueued.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MessageTokenCriteria copy() {
        return new MessageTokenCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getTimeSent() {
        return timeSent;
    }

    public LongFilter timeSent() {
        if (timeSent == null) {
            timeSent = new LongFilter();
        }
        return timeSent;
    }

    public void setTimeSent(LongFilter timeSent) {
        this.timeSent = timeSent;
    }

    public StringFilter getTokenValue() {
        return tokenValue;
    }

    public StringFilter tokenValue() {
        if (tokenValue == null) {
            tokenValue = new StringFilter();
        }
        return tokenValue;
    }

    public void setTokenValue(StringFilter tokenValue) {
        this.tokenValue = tokenValue;
    }

    public BooleanFilter getReceived() {
        return received;
    }

    public BooleanFilter received() {
        if (received == null) {
            received = new BooleanFilter();
        }
        return received;
    }

    public void setReceived(BooleanFilter received) {
        this.received = received;
    }

    public BooleanFilter getActioned() {
        return actioned;
    }

    public BooleanFilter actioned() {
        if (actioned == null) {
            actioned = new BooleanFilter();
        }
        return actioned;
    }

    public void setActioned(BooleanFilter actioned) {
        this.actioned = actioned;
    }

    public BooleanFilter getContentFullyEnqueued() {
        return contentFullyEnqueued;
    }

    public BooleanFilter contentFullyEnqueued() {
        if (contentFullyEnqueued == null) {
            contentFullyEnqueued = new BooleanFilter();
        }
        return contentFullyEnqueued;
    }

    public void setContentFullyEnqueued(BooleanFilter contentFullyEnqueued) {
        this.contentFullyEnqueued = contentFullyEnqueued;
    }

    public LongFilter getPlaceholderId() {
        return placeholderId;
    }

    public LongFilter placeholderId() {
        if (placeholderId == null) {
            placeholderId = new LongFilter();
        }
        return placeholderId;
    }

    public void setPlaceholderId(LongFilter placeholderId) {
        this.placeholderId = placeholderId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
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
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(timeSent, that.timeSent) &&
            Objects.equals(tokenValue, that.tokenValue) &&
            Objects.equals(received, that.received) &&
            Objects.equals(actioned, that.actioned) &&
            Objects.equals(contentFullyEnqueued, that.contentFullyEnqueued) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, timeSent, tokenValue, received, actioned, contentFullyEnqueued, placeholderId, distinct);
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
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
