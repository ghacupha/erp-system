export interface IFxTransactionRateType {
  id?: number;
  fxTransactionRateTypeCode?: string;
  fxTransactionRateType?: string;
  fxTransactionRateTypeDetails?: string | null;
}

export class FxTransactionRateType implements IFxTransactionRateType {
  constructor(
    public id?: number,
    public fxTransactionRateTypeCode?: string,
    public fxTransactionRateType?: string,
    public fxTransactionRateTypeDetails?: string | null
  ) {}
}

export function getFxTransactionRateTypeIdentifier(fxTransactionRateType: IFxTransactionRateType): number | undefined {
  return fxTransactionRateType.id;
}
