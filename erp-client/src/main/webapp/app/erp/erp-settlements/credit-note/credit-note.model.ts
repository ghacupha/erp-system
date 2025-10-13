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
import { IPurchaseOrder } from '../purchase-order/purchase-order.model';
import { IPaymentInvoice } from '../payment-invoice/payment-invoice.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { IPaymentLabel } from '../../erp-pages/payment-label/payment-label.model';

export interface ICreditNote {
  id?: number;
  creditNumber?: string;
  creditNoteDate?: dayjs.Dayjs;
  creditAmount?: number;
  remarks?: string | null;
  purchaseOrders?: IPurchaseOrder[] | null;
  invoices?: IPaymentInvoice[] | null;
  paymentLabels?: IPaymentLabel[] | null;
  placeholders?: IPlaceholder[] | null;
}

export class CreditNote implements ICreditNote {
  constructor(
    public id?: number,
    public creditNumber?: string,
    public creditNoteDate?: dayjs.Dayjs,
    public creditAmount?: number,
    public remarks?: string | null,
    public purchaseOrders?: IPurchaseOrder[] | null,
    public invoices?: IPaymentInvoice[] | null,
    public paymentLabels?: IPaymentLabel[] | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getCreditNoteIdentifier(creditNote: ICreditNote): number | undefined {
  return creditNote.id;
}
