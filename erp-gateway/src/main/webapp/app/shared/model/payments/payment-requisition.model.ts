///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { IPayment } from 'app/shared/model/payments/payment.model';

export interface IPaymentRequisition {
  id?: number;
  dealerName?: string;
  invoicedAmount?: number;
  disbursementCost?: number;
  vatableAmount?: number;
  requisitions?: IPayment[];
}

export class PaymentRequisition implements IPaymentRequisition {
  constructor(
    public id?: number,
    public dealerName?: string,
    public invoicedAmount?: number,
    public disbursementCost?: number,
    public vatableAmount?: number,
    public requisitions?: IPayment[]
  ) {}
}
