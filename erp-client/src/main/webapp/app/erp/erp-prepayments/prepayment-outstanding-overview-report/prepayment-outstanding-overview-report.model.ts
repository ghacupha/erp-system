///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

export interface IPrepaymentOutstandingOverviewReport {
  id?: number;
  totalPrepaymentAmount?: number | null;
  totalAmortisedAmount?: number | null;
  totalOutstandingAmount?: number | null;
  numberOfPrepaymentAccounts?: number | null;
}

export class PrepaymentOutstandingOverviewReport implements IPrepaymentOutstandingOverviewReport {
  constructor(
    public id?: number,
    public totalPrepaymentAmount?: number | null,
    public totalAmortisedAmount?: number | null,
    public totalOutstandingAmount?: number | null,
    public numberOfPrepaymentAccounts?: number | null
  ) {}
}

export function getPrepaymentOutstandingOverviewReportIdentifier(
  prepaymentOutstandingOverviewReport: IPrepaymentOutstandingOverviewReport
): number | undefined {
  return prepaymentOutstandingOverviewReport.id;
}
