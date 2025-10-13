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

import { CurrencyServiceabilityFlagTypes } from 'app/entities/enumerations/currency-serviceability-flag-types.model';
import { CurrencyServiceability } from 'app/entities/enumerations/currency-serviceability.model';

export interface ICurrencyServiceabilityFlag {
  id?: number;
  currencyServiceabilityFlag?: CurrencyServiceabilityFlagTypes;
  currencyServiceability?: CurrencyServiceability;
  currencyServiceabilityFlagDetails?: string | null;
}

export class CurrencyServiceabilityFlag implements ICurrencyServiceabilityFlag {
  constructor(
    public id?: number,
    public currencyServiceabilityFlag?: CurrencyServiceabilityFlagTypes,
    public currencyServiceability?: CurrencyServiceability,
    public currencyServiceabilityFlagDetails?: string | null
  ) {}
}

export function getCurrencyServiceabilityFlagIdentifier(currencyServiceabilityFlag: ICurrencyServiceabilityFlag): number | undefined {
  return currencyServiceabilityFlag.id;
}
