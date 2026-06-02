import { CurrencyAuthenticityFlags } from 'app/entities/enumerations/currency-authenticity-flags.model';
import { CurrencyAuthenticityTypes } from 'app/entities/enumerations/currency-authenticity-types.model';

export interface ICurrencyAuthenticityFlag {
  id?: number;
  currencyAuthenticityFlag?: CurrencyAuthenticityFlags;
  currencyAuthenticityType?: CurrencyAuthenticityTypes;
  currencyAuthenticityTypeDetails?: string | null;
}

export class CurrencyAuthenticityFlag implements ICurrencyAuthenticityFlag {
  constructor(
    public id?: number,
    public currencyAuthenticityFlag?: CurrencyAuthenticityFlags,
    public currencyAuthenticityType?: CurrencyAuthenticityTypes,
    public currencyAuthenticityTypeDetails?: string | null
  ) {}
}

export function getCurrencyAuthenticityFlagIdentifier(currencyAuthenticityFlag: ICurrencyAuthenticityFlag): number | undefined {
  return currencyAuthenticityFlag.id;
}
