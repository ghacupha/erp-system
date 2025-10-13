///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import * as dayjs from 'dayjs';

export interface IPrepaymentReport {
  id?: number;
  catalogueNumber?: string | null;
  particulars?: string | null;
  dealerName?: string | null;
  paymentNumber?: string | null;
  paymentDate?: dayjs.Dayjs | null;
  currencyCode?: string | null;
  prepaymentAmount?: number | null;
  amortisedAmount?: number | null;
  outstandingAmount?: number | null;
}

export class PrepaymentReport implements IPrepaymentReport {
  constructor(
    public id?: number,
    public catalogueNumber?: string | null,
    public particulars?: string | null,
    public dealerName?: string | null,
    public paymentNumber?: string | null,
    public paymentDate?: dayjs.Dayjs | null,
    public currencyCode?: string | null,
    public prepaymentAmount?: number | null,
    public amortisedAmount?: number | null,
    public outstandingAmount?: number | null
  ) {}
}

export function getPrepaymentReportIdentifier(prepaymentReport: IPrepaymentReport): number | undefined {
  return prepaymentReport.id;
}
