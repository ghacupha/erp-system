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

export interface ILeaseLiabilityScheduleReportItem {
  id?: number;
  sequenceNumber?: number | null;
  openingBalance?: number | null;
  cashPayment?: number | null;
  principalPayment?: number | null;
  interestPayment?: number | null;
  outstandingBalance?: number | null;
  interestPayableOpening?: number | null;
  interestAccrued?: number | null;
  interestPayableClosing?: number | null;
  amortizationScheduleId?: number | null;
}

export class LeaseLiabilityScheduleReportItem implements ILeaseLiabilityScheduleReportItem {
  constructor(
    public id?: number,
    public sequenceNumber?: number | null,
    public openingBalance?: number | null,
    public cashPayment?: number | null,
    public principalPayment?: number | null,
    public interestPayment?: number | null,
    public outstandingBalance?: number | null,
    public interestPayableOpening?: number | null,
    public interestAccrued?: number | null,
    public interestPayableClosing?: number | null,
    public amortizationScheduleId?: number | null
  ) {}
}

export function getLeaseLiabilityScheduleReportItemIdentifier(
  leaseLiabilityScheduleReportItem: ILeaseLiabilityScheduleReportItem
): number | undefined {
  return leaseLiabilityScheduleReportItem.id;
}
