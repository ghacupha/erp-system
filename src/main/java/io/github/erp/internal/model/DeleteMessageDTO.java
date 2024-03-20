package io.github.erp.internal.model;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
import io.github.erp.internal.framework.model.TokenizableMessage;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * This is a message transfer object for delete requests where an Id of an entity to be deleted is given.
 * The id to be deleted is here represented by the field intuitively named as id
 */
@XmlRootElement
public class DeleteMessageDTO implements TokenizableMessage<String>, Serializable {
    private static final long serialVersionUID = 7581295114110315260L;
    private long id;
    private String description;
    private String messageToken;
    private long timestamp;

    public DeleteMessageDTO(long id, String description, String messageToken, long timestamp) {
        this.id = id;
        this.description = description;
        this.messageToken = messageToken;
        this.timestamp = timestamp;
    }

    public DeleteMessageDTO() {
    }

    public static DeleteMessageDTOBuilder builder() {
        return new DeleteMessageDTOBuilder();
    }

    public long getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public String getMessageToken() {
        return this.messageToken;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMessageToken(String messageToken) {
        this.messageToken = messageToken;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DeleteMessageDTO)) return false;
        final DeleteMessageDTO other = (DeleteMessageDTO) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getId() != other.getId()) return false;
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description))
            return false;
        final Object this$messageToken = this.getMessageToken();
        final Object other$messageToken = other.getMessageToken();
        if (this$messageToken == null ? other$messageToken != null : !this$messageToken.equals(other$messageToken))
            return false;
        if (this.getTimestamp() != other.getTimestamp()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DeleteMessageDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $id = this.getId();
        result = result * PRIME + (int) ($id >>> 32 ^ $id);
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $messageToken = this.getMessageToken();
        result = result * PRIME + ($messageToken == null ? 43 : $messageToken.hashCode());
        final long $timestamp = this.getTimestamp();
        result = result * PRIME + (int) ($timestamp >>> 32 ^ $timestamp);
        return result;
    }

    public String toString() {
        return "DeleteMessageDTO(id=" + this.getId() + ", description=" + this.getDescription() + ", messageToken=" + this.getMessageToken() + ", timestamp=" + this.getTimestamp() + ")";
    }

    public static class DeleteMessageDTOBuilder {
        private long id;
        private String description;
        private String messageToken;
        private long timestamp;

        DeleteMessageDTOBuilder() {
        }

        public DeleteMessageDTOBuilder id(long id) {
            this.id = id;
            return this;
        }

        public DeleteMessageDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public DeleteMessageDTOBuilder messageToken(String messageToken) {
            this.messageToken = messageToken;
            return this;
        }

        public DeleteMessageDTOBuilder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public DeleteMessageDTO build() {
            return new DeleteMessageDTO(id, description, messageToken, timestamp);
        }

        public String toString() {
            return "DeleteMessageDTO.DeleteMessageDTOBuilder(id=" + this.id + ", description=" + this.description + ", messageToken=" + this.messageToken + ", timestamp=" + this.timestamp + ")";
        }
    }
}
