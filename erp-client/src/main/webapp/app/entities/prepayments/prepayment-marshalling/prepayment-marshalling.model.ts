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

import { IPrepaymentAccount } from 'app/entities/prepayments/prepayment-account/prepayment-account.model';
import { IAmortizationPeriod } from 'app/entities/prepayments/amortization-period/amortization-period.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';

export interface IPrepaymentMarshalling {
  id?: number;
  inactive?: boolean;
  amortizationPeriods?: number | null;
  processed?: boolean | null;
  prepaymentAccount?: IPrepaymentAccount;
  firstAmortizationPeriod?: IAmortizationPeriod;
  placeholders?: IPlaceholder[] | null;
  firstFiscalMonth?: IFiscalMonth;
  lastFiscalMonth?: IFiscalMonth;
}

export class PrepaymentMarshalling implements IPrepaymentMarshalling {
  constructor(
    public id?: number,
    public inactive?: boolean,
    public amortizationPeriods?: number | null,
    public processed?: boolean | null,
    public prepaymentAccount?: IPrepaymentAccount,
    public firstAmortizationPeriod?: IAmortizationPeriod,
    public placeholders?: IPlaceholder[] | null,
    public firstFiscalMonth?: IFiscalMonth,
    public lastFiscalMonth?: IFiscalMonth
  ) {
    this.inactive = this.inactive ?? false;
    this.processed = this.processed ?? false;
  }
}

export function getPrepaymentMarshallingIdentifier(prepaymentMarshalling: IPrepaymentMarshalling): number | undefined {
  return prepaymentMarshalling.id;
}
