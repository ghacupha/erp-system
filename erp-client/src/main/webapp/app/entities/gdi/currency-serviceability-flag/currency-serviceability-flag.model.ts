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
