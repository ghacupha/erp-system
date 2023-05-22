package io.github.erp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link io.github.erp.domain.MfbBranchCode} entity.
 */
public class MfbBranchCodeDTO implements Serializable {

    private Long id;

    private String bankCode;

    private String bankName;

    private String branchCode;

    private String branchName;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
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
        if (!(o instanceof MfbBranchCodeDTO)) {
            return false;
        }

        MfbBranchCodeDTO mfbBranchCodeDTO = (MfbBranchCodeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mfbBranchCodeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MfbBranchCodeDTO{" +
            "id=" + getId() +
            ", bankCode='" + getBankCode() + "'" +
            ", bankName='" + getBankName() + "'" +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchName='" + getBranchName() + "'" +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
