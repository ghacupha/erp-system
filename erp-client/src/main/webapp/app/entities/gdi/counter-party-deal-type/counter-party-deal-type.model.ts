export interface ICounterPartyDealType {
  id?: number;
  counterpartyDealCode?: string;
  counterpartyDealTypeDetails?: string;
  counterpartyDealTypeDescription?: string | null;
}

export class CounterPartyDealType implements ICounterPartyDealType {
  constructor(
    public id?: number,
    public counterpartyDealCode?: string,
    public counterpartyDealTypeDetails?: string,
    public counterpartyDealTypeDescription?: string | null
  ) {}
}

export function getCounterPartyDealTypeIdentifier(counterPartyDealType: ICounterPartyDealType): number | undefined {
  return counterPartyDealType.id;
}
