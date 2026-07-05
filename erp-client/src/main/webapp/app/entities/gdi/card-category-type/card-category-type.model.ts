import { CardCategoryFlag } from 'app/entities/enumerations/card-category-flag.model';

export interface ICardCategoryType {
  id?: number;
  cardCategoryFlag?: CardCategoryFlag;
  cardCategoryDescription?: string;
  cardCategoryDetails?: string | null;
}

export class CardCategoryType implements ICardCategoryType {
  constructor(
    public id?: number,
    public cardCategoryFlag?: CardCategoryFlag,
    public cardCategoryDescription?: string,
    public cardCategoryDetails?: string | null
  ) {}
}

export function getCardCategoryTypeIdentifier(cardCategoryType: ICardCategoryType): number | undefined {
  return cardCategoryType.id;
}
