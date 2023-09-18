package io.github.erp.domain;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A ChartOfAccountsCode.
 */
@Entity
@Table(name = "chart_of_accounts_code")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "chartofaccountscode")
public class ChartOfAccountsCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "chart_of_accounts_code", nullable = false, unique = true)
    private String chartOfAccountsCode;

    @NotNull
    @Column(name = "chart_of_accounts_class", nullable = false)
    private String chartOfAccountsClass;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChartOfAccountsCode id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChartOfAccountsCode() {
        return this.chartOfAccountsCode;
    }

    public ChartOfAccountsCode chartOfAccountsCode(String chartOfAccountsCode) {
        this.setChartOfAccountsCode(chartOfAccountsCode);
        return this;
    }

    public void setChartOfAccountsCode(String chartOfAccountsCode) {
        this.chartOfAccountsCode = chartOfAccountsCode;
    }

    public String getChartOfAccountsClass() {
        return this.chartOfAccountsClass;
    }

    public ChartOfAccountsCode chartOfAccountsClass(String chartOfAccountsClass) {
        this.setChartOfAccountsClass(chartOfAccountsClass);
        return this;
    }

    public void setChartOfAccountsClass(String chartOfAccountsClass) {
        this.chartOfAccountsClass = chartOfAccountsClass;
    }

    public String getDescription() {
        return this.description;
    }

    public ChartOfAccountsCode description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChartOfAccountsCode)) {
            return false;
        }
        return id != null && id.equals(((ChartOfAccountsCode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChartOfAccountsCode{" +
            "id=" + getId() +
            ", chartOfAccountsCode='" + getChartOfAccountsCode() + "'" +
            ", chartOfAccountsClass='" + getChartOfAccountsClass() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
