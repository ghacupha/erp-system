export interface ICounterpartyType {
  id?: number;
  counterpartyTypeCode?: string;
  counterPartyType?: string;
  counterpartyTypeDescription?: string | null;
}

export class CounterpartyType implements ICounterpartyType {
  constructor(
    public id?: number,
    public counterpartyTypeCode?: string,
    public counterPartyType?: string,
    public counterpartyTypeDescription?: string | null
  ) {}
}

export function getCounterpartyTypeIdentifier(counterpartyType: ICounterpartyType): number | undefined {
  return counterpartyType.id;
}
