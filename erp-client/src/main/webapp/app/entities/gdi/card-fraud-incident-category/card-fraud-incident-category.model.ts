export interface ICardFraudIncidentCategory {
  id?: number;
  cardFraudCategoryTypeCode?: string;
  cardFraudCategoryType?: string;
  cardFraudCategoryTypeDescription?: string | null;
}

export class CardFraudIncidentCategory implements ICardFraudIncidentCategory {
  constructor(
    public id?: number,
    public cardFraudCategoryTypeCode?: string,
    public cardFraudCategoryType?: string,
    public cardFraudCategoryTypeDescription?: string | null
  ) {}
}

export function getCardFraudIncidentCategoryIdentifier(cardFraudIncidentCategory: ICardFraudIncidentCategory): number | undefined {
  return cardFraudIncidentCategory.id;
}
