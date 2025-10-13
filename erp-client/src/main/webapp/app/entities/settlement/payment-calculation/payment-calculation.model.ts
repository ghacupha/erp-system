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

import { IPaymentLabel } from 'app/entities/settlement/payment-label/payment-label.model';
import { IPaymentCategory } from 'app/entities/settlement/payment-category/payment-category.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IPaymentCalculation {
  id?: number;
  paymentExpense?: number | null;
  withholdingVAT?: number | null;
  withholdingTax?: number | null;
  paymentAmount?: number | null;
  fileUploadToken?: string | null;
  compilationToken?: string | null;
  paymentLabels?: IPaymentLabel[] | null;
  paymentCategory?: IPaymentCategory | null;
  placeholders?: IPlaceholder[] | null;
}

export class PaymentCalculation implements IPaymentCalculation {
  constructor(
    public id?: number,
    public paymentExpense?: number | null,
    public withholdingVAT?: number | null,
    public withholdingTax?: number | null,
    public paymentAmount?: number | null,
    public fileUploadToken?: string | null,
    public compilationToken?: string | null,
    public paymentLabels?: IPaymentLabel[] | null,
    public paymentCategory?: IPaymentCategory | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getPaymentCalculationIdentifier(paymentCalculation: IPaymentCalculation): number | undefined {
  return paymentCalculation.id;
}
