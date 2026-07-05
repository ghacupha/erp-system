package io.github.erp.internal.repository;

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
import io.github.erp.domain.AmortizationPeriod;
import io.github.erp.domain.FiscalMonth;
import io.github.erp.repository.FiscalMonthRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface InternalFiscalMonthRepository extends
    FiscalMonthRepository,
    JpaRepository<FiscalMonth, Long>,
    JpaSpecificationExecutor<FiscalMonth> {

    Optional<FiscalMonth> findFiscalMonthByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);

    /**
     * Retrieve the nth item in the sequence after the current instance given the
     * currentPeriod_id
     *
     * @param currentPeriodId of the fiscal-month
     * @param nthValue number of lapsed periods
     * @return
     */
    @Query(
        nativeQuery = true,
        value = "" +
            "SELECT * " +
            "FROM fiscal_month " +
            "WHERE id = ( " +
            "    SELECT id + :n_value - 1 " +
            "    FROM fiscal_month " +
            "    WHERE id = :currentFiscalMonthId " +
            ")"
    )
    Optional<FiscalMonth> getNextFiscalPeriodAfterLapsedMonths(@Param("currentFiscalMonthId") long currentPeriodId, @Param("n_value") long nthValue);


}
