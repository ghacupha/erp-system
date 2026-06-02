export interface ICrbAgingBands {
  id?: number;
  agingBandCategoryCode?: string;
  agingBandCategory?: string;
  agingBandCategoryDetails?: string;
}

export class CrbAgingBands implements ICrbAgingBands {
  constructor(
    public id?: number,
    public agingBandCategoryCode?: string,
    public agingBandCategory?: string,
    public agingBandCategoryDetails?: string
  ) {}
}

export function getCrbAgingBandsIdentifier(crbAgingBands: ICrbAgingBands): number | undefined {
  return crbAgingBands.id;
}
