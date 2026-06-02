export interface ICardTypes {
  id?: number;
  cardTypeCode?: string;
  cardType?: string;
  cardTypeDetails?: string | null;
}

export class CardTypes implements ICardTypes {
  constructor(public id?: number, public cardTypeCode?: string, public cardType?: string, public cardTypeDetails?: string | null) {}
}

export function getCardTypesIdentifier(cardTypes: ICardTypes): number | undefined {
  return cardTypes.id;
}
