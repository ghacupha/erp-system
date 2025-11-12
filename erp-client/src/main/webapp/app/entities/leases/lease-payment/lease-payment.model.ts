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

import * as dayjs from 'dayjs';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';

export interface ILeasePayment {
  id?: number;
  paymentDate?: dayjs.Dayjs | null;
  paymentAmount?: number | null;
  leaseContract?: IIFRS16LeaseContract;
}

export class LeasePayment implements ILeasePayment {
  constructor(
    public id?: number,
    public paymentDate?: dayjs.Dayjs | null,
    public paymentAmount?: number | null,
    public leaseContract?: IIFRS16LeaseContract
  ) {}
}

export function getLeasePaymentIdentifier(leasePayment: ILeasePayment): number | undefined {
  return leasePayment.id;
}
