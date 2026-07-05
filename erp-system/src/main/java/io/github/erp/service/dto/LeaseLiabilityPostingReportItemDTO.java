package io.github.erp.service.dto;

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
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.erp.domain.LeaseLiabilityPostingReportItem} entity.
 */
public class LeaseLiabilityPostingReportItemDTO implements Serializable {

    private Long id;

    private String bookingId;

    private String leaseTitle;

    private String leaseDescription;

    private String accountNumber;

    private String posting;

    private BigDecimal postingAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getLeaseTitle() {
        return leaseTitle;
    }

    public void setLeaseTitle(String leaseTitle) {
        this.leaseTitle = leaseTitle;
    }

    public String getLeaseDescription() {
        return leaseDescription;
    }

    public void setLeaseDescription(String leaseDescription) {
        this.leaseDescription = leaseDescription;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPosting() {
        return posting;
    }

    public void setPosting(String posting) {
        this.posting = posting;
    }

    public BigDecimal getPostingAmount() {
        return postingAmount;
    }

    public void setPostingAmount(BigDecimal postingAmount) {
        this.postingAmount = postingAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseLiabilityPostingReportItemDTO)) {
            return false;
        }

        LeaseLiabilityPostingReportItemDTO leaseLiabilityPostingReportItemDTO = (LeaseLiabilityPostingReportItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaseLiabilityPostingReportItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseLiabilityPostingReportItemDTO{" +
            "id=" + getId() +
            ", bookingId='" + getBookingId() + "'" +
            ", leaseTitle='" + getLeaseTitle() + "'" +
            ", leaseDescription='" + getLeaseDescription() + "'" +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", posting='" + getPosting() + "'" +
            ", postingAmount=" + getPostingAmount() +
            "}";
    }
}
