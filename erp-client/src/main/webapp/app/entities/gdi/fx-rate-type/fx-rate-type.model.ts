export interface IFxRateType {
  id?: number;
  fxRateCode?: string;
  fxRateType?: string;
  fxRateDetails?: string | null;
}

export class FxRateType implements IFxRateType {
  constructor(public id?: number, public fxRateCode?: string, public fxRateType?: string, public fxRateDetails?: string | null) {}
}

export function getFxRateTypeIdentifier(fxRateType: IFxRateType): number | undefined {
  return fxRateType.id;
}
