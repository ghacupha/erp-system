///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

export interface ILeaseLiabilityReportItem {
  id?: number;
  bookingId?: string | null;
  leaseTitle?: string | null;
  liabilityAccountNumber?: string | null;
  liabilityAmount?: number | null;
  interestPayableAccountNumber?: string | null;
  interestPayableAmount?: number | null;
}

export class LeaseLiabilityReportItem implements ILeaseLiabilityReportItem {
  constructor(
    public id?: number,
    public bookingId?: string | null,
    public leaseTitle?: string | null,
    public liabilityAccountNumber?: string | null,
    public liabilityAmount?: number | null,
    public interestPayableAccountNumber?: string | null,
    public interestPayableAmount?: number | null
  ) {}
}

export function getLeaseLiabilityReportItemIdentifier(leaseLiabilityReportItem: ILeaseLiabilityReportItem): number | undefined {
  return leaseLiabilityReportItem.id;
}
