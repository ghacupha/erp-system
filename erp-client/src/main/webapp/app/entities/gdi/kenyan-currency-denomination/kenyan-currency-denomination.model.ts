export interface IKenyanCurrencyDenomination {
  id?: number;
  currencyDenominationCode?: string;
  currencyDenominationType?: string;
  currencyDenominationTypeDetails?: string | null;
}

export class KenyanCurrencyDenomination implements IKenyanCurrencyDenomination {
  constructor(
    public id?: number,
    public currencyDenominationCode?: string,
    public currencyDenominationType?: string,
    public currencyDenominationTypeDetails?: string | null
  ) {}
}

export function getKenyanCurrencyDenominationIdentifier(kenyanCurrencyDenomination: IKenyanCurrencyDenomination): number | undefined {
  return kenyanCurrencyDenomination.id;
}
