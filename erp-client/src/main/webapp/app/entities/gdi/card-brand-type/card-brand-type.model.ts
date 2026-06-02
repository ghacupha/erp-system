export interface ICardBrandType {
  id?: number;
  cardBrandTypeCode?: string;
  cardBrandType?: string;
  cardBrandTypeDetails?: string | null;
}

export class CardBrandType implements ICardBrandType {
  constructor(
    public id?: number,
    public cardBrandTypeCode?: string,
    public cardBrandType?: string,
    public cardBrandTypeDetails?: string | null
  ) {}
}

export function getCardBrandTypeIdentifier(cardBrandType: ICardBrandType): number | undefined {
  return cardBrandType.id;
}
