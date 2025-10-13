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
import { IPlaceholder } from 'app/erp/erp-pages/placeholder/placeholder.model';
import { IPaymentLabel } from '../../payment-label/payment-label.model';

export interface IPaymentRequisition {
  id?: number;
  receptionDate?: dayjs.Dayjs | null;
  dealerName?: string | null;
  briefDescription?: string | null;
  requisitionNumber?: string | null;
  invoicedAmount?: number | null;
  disbursementCost?: number | null;
  taxableAmount?: number | null;
  requisitionProcessed?: boolean | null;
  fileUploadToken?: string | null;
  compilationToken?: string | null;
  paymentLabels?: IPaymentLabel[] | null;
  placeholders?: IPlaceholder[] | null;
}

export class PaymentRequisition implements IPaymentRequisition {
  constructor(
    public id?: number,
    public receptionDate?: dayjs.Dayjs | null,
    public dealerName?: string | null,
    public briefDescription?: string | null,
    public requisitionNumber?: string | null,
    public invoicedAmount?: number | null,
    public disbursementCost?: number | null,
    public taxableAmount?: number | null,
    public requisitionProcessed?: boolean | null,
    public fileUploadToken?: string | null,
    public compilationToken?: string | null,
    public paymentLabels?: IPaymentLabel[] | null,
    public placeholders?: IPlaceholder[] | null
  ) {
    this.requisitionProcessed = this.requisitionProcessed ?? false;
  }
}

export function getPaymentRequisitionIdentifier(paymentRequisition: IPaymentRequisition): number | undefined {
  return paymentRequisition.id;
}
