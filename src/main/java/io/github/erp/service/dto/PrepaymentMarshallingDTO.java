package io.github.erp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.PrepaymentMarshalling} entity.
 */
public class PrepaymentMarshallingDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean inactive;

    private LocalDate amortizationCommencementDate;

    private Integer amortizationPeriods;

    private PrepaymentAccountDTO prepaymentAccount;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public LocalDate getAmortizationCommencementDate() {
        return amortizationCommencementDate;
    }

    public void setAmortizationCommencementDate(LocalDate amortizationCommencementDate) {
        this.amortizationCommencementDate = amortizationCommencementDate;
    }

    public Integer getAmortizationPeriods() {
        return amortizationPeriods;
    }

    public void setAmortizationPeriods(Integer amortizationPeriods) {
        this.amortizationPeriods = amortizationPeriods;
    }

    public PrepaymentAccountDTO getPrepaymentAccount() {
        return prepaymentAccount;
    }

    public void setPrepaymentAccount(PrepaymentAccountDTO prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
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
        if (!(o instanceof PrepaymentMarshallingDTO)) {
            return false;
        }

        PrepaymentMarshallingDTO prepaymentMarshallingDTO = (PrepaymentMarshallingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prepaymentMarshallingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrepaymentMarshallingDTO{" +
            "id=" + getId() +
            ", inactive='" + getInactive() + "'" +
            ", amortizationCommencementDate='" + getAmortizationCommencementDate() + "'" +
            ", amortizationPeriods=" + getAmortizationPeriods() +
            ", prepaymentAccount=" + getPrepaymentAccount() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
