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
import { IDealer } from '../../erp-pages/dealers/dealer/dealer.model';
import { IBusinessStamp } from '../business-stamp/business-stamp.model';
import { IPaymentLabel } from '../../erp-pages/payment-label/payment-label.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';

export interface IJobSheet {
  id?: number;
  serialNumber?: string;
  jobSheetDate?: dayjs.Dayjs | null;
  details?: string | null;
  remarks?: string | null;
  biller?: IDealer;
  signatories?: IDealer[] | null;
  contactPerson?: IDealer | null;
  businessStamps?: IBusinessStamp[] | null;
  placeholders?: IPlaceholder[] | null;
  paymentLabels?: IPaymentLabel[] | null;
}

export class JobSheet implements IJobSheet {
  constructor(
    public id?: number,
    public serialNumber?: string,
    public jobSheetDate?: dayjs.Dayjs | null,
    public details?: string | null,
    public remarks?: string | null,
    public biller?: IDealer,
    public signatories?: IDealer[] | null,
    public contactPerson?: IDealer | null,
    public businessStamps?: IBusinessStamp[] | null,
    public placeholders?: IPlaceholder[] | null,
    public paymentLabels?: IPaymentLabel[] | null
  ) {}
}

export function getJobSheetIdentifier(jobSheet: IJobSheet): number | undefined {
  return jobSheet.id;
}
