package io.github.erp.internal.service.posting;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.Placeholder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PostingContext {

    private final String module;
    private final String eventType;
    private final String varianceType;
    private final String invoiceTiming;
    private final Map<String, String> attributes;
    private final BigDecimal amount;
    private final LocalDate transactionDate;
    private final String description;
    private final String transactionType;
    private final UUID postingId;
    private final ApplicationUser postedBy;
    private final Set<Placeholder> placeholders;

    private PostingContext(Builder builder) {
        this.module = builder.module;
        this.eventType = builder.eventType;
        this.varianceType = builder.varianceType;
        this.invoiceTiming = builder.invoiceTiming;
        this.attributes = Collections.unmodifiableMap(new HashMap<>(builder.attributes));
        this.amount = builder.amount;
        this.transactionDate = builder.transactionDate;
        this.description = builder.description;
        this.transactionType = builder.transactionType;
        this.postingId = builder.postingId;
        this.postedBy = builder.postedBy;
        this.placeholders = builder.placeholders;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getModule() {
        return module;
    }

    public String getEventType() {
        return eventType;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public String getInvoiceTiming() {
        return invoiceTiming;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public UUID getPostingId() {
        return postingId;
    }

    public ApplicationUser getPostedBy() {
        return postedBy;
    }

    public Set<Placeholder> getPlaceholders() {
        return placeholders;
    }

    public static class Builder {

        private String module;
        private String eventType;
        private String varianceType;
        private String invoiceTiming;
        private Map<String, String> attributes = new HashMap<>();
        private BigDecimal amount;
        private LocalDate transactionDate;
        private String description;
        private String transactionType;
        private UUID postingId;
        private ApplicationUser postedBy;
        private Set<Placeholder> placeholders;

        public Builder module(String module) {
            this.module = module;
            return this;
        }

        public Builder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder varianceType(String varianceType) {
            this.varianceType = varianceType;
            return this;
        }

        public Builder invoiceTiming(String invoiceTiming) {
            this.invoiceTiming = invoiceTiming;
            return this;
        }

        public Builder attribute(String key, String value) {
            if (key != null && value != null) {
                this.attributes.put(key, value);
            }
            return this;
        }

        public Builder attributes(Map<String, String> attributes) {
            if (attributes != null) {
                this.attributes.putAll(attributes);
            }
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder transactionDate(LocalDate transactionDate) {
            this.transactionDate = transactionDate;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder transactionType(String transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Builder postingId(UUID postingId) {
            this.postingId = postingId;
            return this;
        }

        public Builder postedBy(ApplicationUser postedBy) {
            this.postedBy = postedBy;
            return this;
        }

        public Builder placeholders(Set<Placeholder> placeholders) {
            this.placeholders = placeholders;
            return this;
        }

        public PostingContext build() {
            return new PostingContext(this);
        }
    }
}
