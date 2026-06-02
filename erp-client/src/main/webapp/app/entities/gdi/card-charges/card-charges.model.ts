export interface ICardCharges {
  id?: number;
  cardChargeType?: string;
  cardChargeTypeName?: string;
  cardChargeDetails?: string | null;
}

export class CardCharges implements ICardCharges {
  constructor(
    public id?: number,
    public cardChargeType?: string,
    public cardChargeTypeName?: string,
    public cardChargeDetails?: string | null
  ) {}
}

export function getCardChargesIdentifier(cardCharges: ICardCharges): number | undefined {
  return cardCharges.id;
}
