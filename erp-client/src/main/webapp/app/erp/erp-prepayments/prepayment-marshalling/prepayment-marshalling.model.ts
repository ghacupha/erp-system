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

import { IPrepaymentAccount } from '../prepayment-account/prepayment-account.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { IFiscalMonth } from '../../erp-pages/fiscal-month/fiscal-month.model';
import { IAmortizationPeriod } from '../amortization-period/amortization-period.model';

export interface IPrepaymentMarshalling {
  id?: number;
  inactive?: boolean;
  amortizationPeriods?: number | null;
  processed?: boolean | null;
  prepaymentAccount?: IPrepaymentAccount;
  placeholders?: IPlaceholder[] | null;
  firstFiscalMonth?: IFiscalMonth;
  lastFiscalMonth?: IFiscalMonth;
  firstAmortizationPeriod?: IAmortizationPeriod;
}

export class PrepaymentMarshalling implements IPrepaymentMarshalling {
  constructor(
    public id?: number,
    public inactive?: boolean,
    public amortizationPeriods?: number | null,
    public processed?: boolean | null,
    public prepaymentAccount?: IPrepaymentAccount,
    public placeholders?: IPlaceholder[] | null,
    public firstFiscalMonth?: IFiscalMonth,
    public lastFiscalMonth?: IFiscalMonth,
    public firstAmortizationPeriod?: IAmortizationPeriod,
  ) {
    this.inactive = this.inactive ?? false;
    this.processed = this.processed ?? false;
  }
}

export function getPrepaymentMarshallingIdentifier(prepaymentMarshalling: IPrepaymentMarshalling): number | undefined {
  return prepaymentMarshalling.id;
}
