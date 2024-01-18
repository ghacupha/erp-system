
/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
package io.github.erp.internal.report.autonomousReport._maps;

import io.github.erp.domain.PrepaymentAccountReportTuple;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.PrepaymentAccountReportDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("prepaymentAccountReportTupleMapper")
public class PrepaymentAccountReportTupleMapper implements Mapping<PrepaymentAccountReportTuple, PrepaymentAccountReportDTO> {

    @Override
    public PrepaymentAccountReportTuple toValue1(PrepaymentAccountReportDTO vs) {
        PrepaymentAccountReportTuple tuple = new PrepaymentAccountReportTuple() {
            @Override
            public Long getId() {
                return vs.getId();
            }

            @Override
            public String getPrepaymentAccount() {
                return vs.getPrepaymentAccount();
            }

            @Override
            public BigDecimal getPrepaymentAmount() {
                return vs.getPrepaymentAmount();
            }

            @Override
            public BigDecimal getAmortisedAmount() {
                return vs.getAmortisedAmount();
            }

            @Override
            public BigDecimal getOutstandingAmount() {
                return vs.getOutstandingAmount();
            }

            @Override
            public Integer getNumberOfPrepaymentAccounts() {
                return vs.getNumberOfPrepaymentAccounts();
            }

            @Override
            public Integer getNumberOfAmortisedItems() {
                return vs.getNumberOfAmortisedItems();
            }
        };

        return tuple;
    }

    @Override
    public PrepaymentAccountReportDTO toValue2(PrepaymentAccountReportTuple vs) {
        PrepaymentAccountReportDTO report = new PrepaymentAccountReportDTO();
        report.setId(vs.getId());
        report.setPrepaymentAccount(vs.getPrepaymentAccount());
        report.setNumberOfPrepaymentAccounts(vs.getNumberOfPrepaymentAccounts());
        report.setNumberOfAmortisedItems(vs.getNumberOfAmortisedItems());
        report.setPrepaymentAmount(vs.getPrepaymentAmount());
        report.setAmortisedAmount(vs.getAmortisedAmount());
        report.setOutstandingAmount(vs.getOutstandingAmount());

        return report;
    }
}
