import { CounterpartyCategory } from 'app/entities/enumerations/counterparty-category.model';

export interface ICounterPartyCategory {
  id?: number;
  counterpartyCategoryCode?: string;
  counterpartyCategoryCodeDetails?: CounterpartyCategory;
  counterpartyCategoryDescription?: string | null;
}

export class CounterPartyCategory implements ICounterPartyCategory {
  constructor(
    public id?: number,
    public counterpartyCategoryCode?: string,
    public counterpartyCategoryCodeDetails?: CounterpartyCategory,
    public counterpartyCategoryDescription?: string | null
  ) {}
}

export function getCounterPartyCategoryIdentifier(counterPartyCategory: ICounterPartyCategory): number | undefined {
  return counterPartyCategory.id;
}
