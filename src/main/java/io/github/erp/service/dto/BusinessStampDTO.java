package io.github.erp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.BusinessStamp} entity.
 */
public class BusinessStampDTO implements Serializable {

    private Long id;

    private LocalDate stampDate;

    private String purpose;

    private String details;

    @Lob
    private String remarks;

    private DealerDTO stampHolder;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStampDate() {
        return stampDate;
    }

    public void setStampDate(LocalDate stampDate) {
        this.stampDate = stampDate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public DealerDTO getStampHolder() {
        return stampHolder;
    }

    public void setStampHolder(DealerDTO stampHolder) {
        this.stampHolder = stampHolder;
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
        if (!(o instanceof BusinessStampDTO)) {
            return false;
        }

        BusinessStampDTO businessStampDTO = (BusinessStampDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, businessStampDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BusinessStampDTO{" +
            "id=" + getId() +
            ", stampDate='" + getStampDate() + "'" +
            ", purpose='" + getPurpose() + "'" +
            ", details='" + getDetails() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", stampHolder=" + getStampHolder() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
