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

export interface IInvoice {
  id?: number;
  invoiceNumber?: string;
  invoiceDate?: Moment;
  invoiceAmount?: number;
  paymentCategory?: string;
  dealerName?: string;
  paymentId?: number;
  dealerId?: number;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public invoiceNumber?: string,
    public invoiceDate?: Moment,
    public invoiceAmount?: number,
    public paymentCategory?: string,
    public dealerName?: string,
    public paymentId?: number,
    public dealerId?: number
  ) {}
}
