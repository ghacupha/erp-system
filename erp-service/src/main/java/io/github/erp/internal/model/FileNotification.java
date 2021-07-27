package io.github.erp.internal.model;

/*-
 * Leassets Server - Leases and assets management platform
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.leassets.domain.enumeration.LeassetsFileModelType;
import io.github.leassets.internal.framework.model.TokenizableMessage;

/**
 * This is a notification which contains metadata of a recently uploaded which a listener might act upon
 */
public class FileNotification implements TokenizableMessage<String> {
    private static final long serialVersionUID = -6472961232578342431L;

    private String fileId;

    private long timestamp;

    private String filename;

    private String messageToken;

    private String description;

    private LeassetsFileModelType leassetsfileModelType;

    public FileNotification(String fileId, long timestamp, String filename, String messageToken, String description, LeassetsFileModelType leassetsfileModelType) {
            this.fileId = fileId;
            this.timestamp = timestamp;
            this.filename = filename;
            this.messageToken = messageToken;
            this.description = description;
            this.leassetsfileModelType = leassetsfileModelType;
        }

        public FileNotification() {
        }

        public static FileNotificationBuilder builder() {
            return new FileNotificationBuilder();
        }

        public String getFileId() {
            return this.fileId;
        }

        public long getTimestamp() {
            return this.timestamp;
        }

        public String getFilename() {
            return this.filename;
        }

        public String getMessageToken() {
            return this.messageToken;
        }

        public String getDescription() {
            return this.description;
        }

        public LeassetsFileModelType getleassetsfileModelType() {
            return this.leassetsfileModelType;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public void setMessageToken(String messageToken) {
            this.messageToken = messageToken;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setleassetsfileModelType(LeassetsFileModelType leassetsfileModelType) {
            this.leassetsfileModelType = leassetsfileModelType;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof FileNotification)) return false;
            final FileNotification other = (FileNotification) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$fileId = this.getFileId();
            final Object other$fileId = other.getFileId();
            if (this$fileId == null ? other$fileId != null : !this$fileId.equals(other$fileId)) return false;
            if (this.getTimestamp() != other.getTimestamp()) return false;
            final Object this$filename = this.getFilename();
            final Object other$filename = other.getFilename();
            if (this$filename == null ? other$filename != null : !this$filename.equals(other$filename)) return false;
            final Object this$messageToken = this.getMessageToken();
            final Object other$messageToken = other.getMessageToken();
            if (this$messageToken == null ? other$messageToken != null : !this$messageToken.equals(other$messageToken))
                return false;
            final Object this$description = this.getDescription();
            final Object other$description = other.getDescription();
            if (this$description == null ? other$description != null : !this$description.equals(other$description))
                return false;
            final Object this$leassetsfileModelType = this.getleassetsfileModelType();
            final Object other$leassetsfileModelType = other.getleassetsfileModelType();
            if (this$leassetsfileModelType == null ? other$leassetsfileModelType != null : !this$leassetsfileModelType.equals(other$leassetsfileModelType))
                return false;
            return true;
        }

        protected boolean canEqual(final Object other) {
            return other instanceof FileNotification;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $fileId = this.getFileId();
            result = result * PRIME + ($fileId == null ? 43 : $fileId.hashCode());
            final long $timestamp = this.getTimestamp();
            result = result * PRIME + (int) ($timestamp >>> 32 ^ $timestamp);
            final Object $filename = this.getFilename();
            result = result * PRIME + ($filename == null ? 43 : $filename.hashCode());
            final Object $messageToken = this.getMessageToken();
            result = result * PRIME + ($messageToken == null ? 43 : $messageToken.hashCode());
            final Object $description = this.getDescription();
            result = result * PRIME + ($description == null ? 43 : $description.hashCode());
            final Object $leassetsfileModelType = this.getleassetsfileModelType();
            result = result * PRIME + ($leassetsfileModelType == null ? 43 : $leassetsfileModelType.hashCode());
            return result;
        }

        public String toString() {
            return "FileNotification(fileId=" + this.getFileId() + ", timestamp=" + this.getTimestamp() + ", filename=" + this.getFilename() + ", messageToken=" + this.getMessageToken() + ", description=" + this.getDescription() + ", leassetsfileModelType=" + this.getleassetsfileModelType() + ")";
        }

        public static class FileNotificationBuilder {
            private String fileId;
            private long timestamp;
            private String filename;
            private String messageToken;
            private String description;
            private LeassetsFileModelType leassetsfileModelType;

            FileNotificationBuilder() {
            }

            public FileNotificationBuilder fileId(String fileId) {
                this.fileId = fileId;
                return this;
            }

            public FileNotificationBuilder timestamp(long timestamp) {
                this.timestamp = timestamp;
                return this;
            }

            public FileNotificationBuilder filename(String filename) {
                this.filename = filename;
                return this;
            }

            public FileNotificationBuilder messageToken(String messageToken) {
                this.messageToken = messageToken;
                return this;
            }

            public FileNotificationBuilder description(String description) {
                this.description = description;
                return this;
            }

            public FileNotificationBuilder leassetsfileModelType(LeassetsFileModelType leassetsfileModelType) {
                this.leassetsfileModelType = leassetsfileModelType;
                return this;
            }

            public FileNotification build() {
                return new FileNotification(fileId, timestamp, filename, messageToken, description, leassetsfileModelType);
            }

            public String toString() {
                return "FileNotification.FileNotificationBuilder(fileId=" + this.fileId + ", timestamp=" + this.timestamp + ", filename=" + this.filename + ", messageToken=" + this.messageToken + ", description=" + this.description + ", leassetsfileModelType=" + this.leassetsfileModelType + ")";
            }
        }
}
