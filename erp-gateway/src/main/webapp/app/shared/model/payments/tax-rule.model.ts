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

export interface ITaxRule {
  id?: number;
  paymentNumber?: string;
  paymentDate?: Moment;
  telcoExciseDuty?: number;
  valueAddedTax?: number;
  withholdingVAT?: number;
  withholdingTaxConsultancy?: number;
  withholdingTaxRent?: number;
  cateringLevy?: number;
  serviceCharge?: number;
  withholdingTaxImportedService?: number;
  paymentId?: number;
}

export class TaxRule implements ITaxRule {
  constructor(
    public id?: number,
    public paymentNumber?: string,
    public paymentDate?: Moment,
    public telcoExciseDuty?: number,
    public valueAddedTax?: number,
    public withholdingVAT?: number,
    public withholdingTaxConsultancy?: number,
    public withholdingTaxRent?: number,
    public cateringLevy?: number,
    public serviceCharge?: number,
    public withholdingTaxImportedService?: number,
    public paymentId?: number
  ) {}
}
