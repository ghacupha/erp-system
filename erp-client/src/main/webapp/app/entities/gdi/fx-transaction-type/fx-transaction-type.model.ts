export interface IFxTransactionType {
  id?: number;
  fxTransactionTypeCode?: string;
  fxTransactionType?: string;
  fxTransactionTypeDescription?: string | null;
}

export class FxTransactionType implements IFxTransactionType {
  constructor(
    public id?: number,
    public fxTransactionTypeCode?: string,
    public fxTransactionType?: string,
    public fxTransactionTypeDescription?: string | null
  ) {}
}

export function getFxTransactionTypeIdentifier(fxTransactionType: IFxTransactionType): number | undefined {
  return fxTransactionType.id;
}
