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
import { IPaymentLabel } from 'app/entities/settlement/payment-label/payment-label.model';
import { IPaymentCategory } from 'app/entities/settlement/payment-category/payment-category.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { CurrencyTypes } from 'app/entities/enumerations/currency-types.model';

export interface ISignedPayment {
  id?: number;
  transactionNumber?: string;
  transactionDate?: dayjs.Dayjs;
  transactionCurrency?: CurrencyTypes;
  transactionAmount?: number;
  dealerName?: string | null;
  fileUploadToken?: string | null;
  compilationToken?: string | null;
  paymentLabels?: IPaymentLabel[] | null;
  paymentCategory?: IPaymentCategory | null;
  placeholders?: IPlaceholder[] | null;
  signedPaymentGroup?: ISignedPayment | null;
}

export class SignedPayment implements ISignedPayment {
  constructor(
    public id?: number,
    public transactionNumber?: string,
    public transactionDate?: dayjs.Dayjs,
    public transactionCurrency?: CurrencyTypes,
    public transactionAmount?: number,
    public dealerName?: string | null,
    public fileUploadToken?: string | null,
    public compilationToken?: string | null,
    public paymentLabels?: IPaymentLabel[] | null,
    public paymentCategory?: IPaymentCategory | null,
    public placeholders?: IPlaceholder[] | null,
    public signedPaymentGroup?: ISignedPayment | null
  ) {}
}

export function getSignedPaymentIdentifier(signedPayment: ISignedPayment): number | undefined {
  return signedPayment.id;
}
