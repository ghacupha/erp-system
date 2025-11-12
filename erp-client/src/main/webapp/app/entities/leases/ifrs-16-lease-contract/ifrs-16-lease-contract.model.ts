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
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';
import { ILeasePayment } from 'app/entities/leases/lease-payment/lease-payment.model';

export interface IIFRS16LeaseContract {
  id?: number;
  bookingId?: string;
  leaseTitle?: string;
  shortTitle?: string | null;
  description?: string | null;
  inceptionDate?: dayjs.Dayjs;
  commencementDate?: dayjs.Dayjs;
  serialNumber?: string | null;
  superintendentServiceOutlet?: IServiceOutlet;
  mainDealer?: IDealer;
  firstReportingPeriod?: IFiscalMonth;
  lastReportingPeriod?: IFiscalMonth;
  leaseContractDocument?: IBusinessDocument | null;
  leaseContractCalculations?: IBusinessDocument | null;
  leasePayments?: ILeasePayment[] | null;
}

export class IFRS16LeaseContract implements IIFRS16LeaseContract {
  constructor(
    public id?: number,
    public bookingId?: string,
    public leaseTitle?: string,
    public shortTitle?: string | null,
    public description?: string | null,
    public inceptionDate?: dayjs.Dayjs,
    public commencementDate?: dayjs.Dayjs,
    public serialNumber?: string | null,
    public superintendentServiceOutlet?: IServiceOutlet,
    public mainDealer?: IDealer,
    public firstReportingPeriod?: IFiscalMonth,
    public lastReportingPeriod?: IFiscalMonth,
    public leaseContractDocument?: IBusinessDocument | null,
    public leaseContractCalculations?: IBusinessDocument | null,
    public leasePayments?: ILeasePayment[] | null
  ) {}
}

export function getIFRS16LeaseContractIdentifier(iFRS16LeaseContract: IIFRS16LeaseContract): number | undefined {
  return iFRS16LeaseContract.id;
}
