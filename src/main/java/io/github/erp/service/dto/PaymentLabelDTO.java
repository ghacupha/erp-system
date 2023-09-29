package io.github.erp.service.dto;

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
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.PaymentLabel} entity.
 */
public class PaymentLabelDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    private String comments;

    private String fileUploadToken;

    private String compilationToken;

    @Lob
    private String remarks;

    private PaymentLabelDTO containingPaymentLabel;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getFileUploadToken() {
        return fileUploadToken;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return compilationToken;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public PaymentLabelDTO getContainingPaymentLabel() {
        return containingPaymentLabel;
    }

    public void setContainingPaymentLabel(PaymentLabelDTO containingPaymentLabel) {
        this.containingPaymentLabel = containingPaymentLabel;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentLabelDTO)) {
            return false;
        }

        PaymentLabelDTO paymentLabelDTO = (PaymentLabelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentLabelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentLabelDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", comments='" + getComments() + "'" +
            ", fileUploadToken='" + getFileUploadToken() + "'" +
            ", compilationToken='" + getCompilationToken() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", containingPaymentLabel=" + getContainingPaymentLabel() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
