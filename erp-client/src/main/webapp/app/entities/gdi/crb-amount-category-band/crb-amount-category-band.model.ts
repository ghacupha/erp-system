export interface ICrbAmountCategoryBand {
  id?: number;
  amountCategoryBandCode?: string;
  amountCategoryBand?: string;
  amountCategoryBandDetails?: string | null;
}

export class CrbAmountCategoryBand implements ICrbAmountCategoryBand {
  constructor(
    public id?: number,
    public amountCategoryBandCode?: string,
    public amountCategoryBand?: string,
    public amountCategoryBandDetails?: string | null
  ) {}
}

export function getCrbAmountCategoryBandIdentifier(crbAmountCategoryBand: ICrbAmountCategoryBand): number | undefined {
  return crbAmountCategoryBand.id;
}
