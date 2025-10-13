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

import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { ILeaseLiability } from '../lease-liability/lease-liability.model';
import { ILeaseRepaymentPeriod } from '../lease-repayment-period/lease-repayment-period.model';
import { IIFRS16LeaseContract } from '../ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IUniversallyUniqueMapping } from '../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';
import { ILeaseAmortizationSchedule } from '../lease-amortization-schedule/lease-amortization-schedule.model';

export interface ILeaseLiabilityScheduleItem {
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
  placeholders?: IPlaceholder[] | null;
  universallyUniqueMappings?: IUniversallyUniqueMapping[] | null;
  leaseAmortizationSchedule?: ILeaseAmortizationSchedule | null;
  leaseContract?: IIFRS16LeaseContract;
  leaseLiability?: ILeaseLiability;
  leasePeriod?: ILeaseRepaymentPeriod;
}

export class LeaseLiabilityScheduleItem implements ILeaseLiabilityScheduleItem {
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
    public placeholders?: IPlaceholder[] | null,
    public universallyUniqueMappings?: IUniversallyUniqueMapping[] | null,
    public leaseAmortizationSchedule?: ILeaseAmortizationSchedule | null,
    public leaseContract?: IIFRS16LeaseContract,
    public leaseLiability?: ILeaseLiability,
    public leasePeriod?: ILeaseRepaymentPeriod
  ) {}
}

export function getLeaseLiabilityScheduleItemIdentifier(leaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem): number | undefined {
  return leaseLiabilityScheduleItem.id;
}
