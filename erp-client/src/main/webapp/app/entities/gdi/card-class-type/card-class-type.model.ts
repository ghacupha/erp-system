export interface ICardClassType {
  id?: number;
  cardClassTypeCode?: string;
  cardClassType?: string;
  cardClassDetails?: string | null;
}

export class CardClassType implements ICardClassType {
  constructor(
    public id?: number,
    public cardClassTypeCode?: string,
    public cardClassType?: string,
    public cardClassDetails?: string | null
  ) {}
}

export function getCardClassTypeIdentifier(cardClassType: ICardClassType): number | undefined {
  return cardClassType.id;
}
