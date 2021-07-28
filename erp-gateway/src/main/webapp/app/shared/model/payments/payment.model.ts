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

import { Moment } from 'moment';
import { IInvoice } from 'app/shared/model/payments/invoice.model';
import { IDealer } from 'app/shared/model/dealers/dealer.model';

export interface IPayment {
  id?: number;
  paymentNumber?: string;
  paymentDate?: Moment;
  paymentAmount?: number;
  dealerName?: string;
  paymentCategory?: string;
  ownedInvoices?: IInvoice[];
  paymentCalculationId?: number;
  paymentRequisitionId?: number;
  dealers?: IDealer[];
  taxRuleId?: number;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public paymentNumber?: string,
    public paymentDate?: Moment,
    public paymentAmount?: number,
    public dealerName?: string,
    public paymentCategory?: string,
    public ownedInvoices?: IInvoice[],
    public paymentCalculationId?: number,
    public paymentRequisitionId?: number,
    public dealers?: IDealer[],
    public taxRuleId?: number
  ) {}
}
