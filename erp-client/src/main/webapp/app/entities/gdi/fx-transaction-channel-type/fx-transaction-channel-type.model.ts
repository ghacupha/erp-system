export interface IFxTransactionChannelType {
  id?: number;
  fxTransactionChannelCode?: string;
  fxTransactionChannelType?: string;
  fxChannelTypeDetails?: string | null;
}

export class FxTransactionChannelType implements IFxTransactionChannelType {
  constructor(
    public id?: number,
    public fxTransactionChannelCode?: string,
    public fxTransactionChannelType?: string,
    public fxChannelTypeDetails?: string | null
  ) {}
}

export function getFxTransactionChannelTypeIdentifier(fxTransactionChannelType: IFxTransactionChannelType): number | undefined {
  return fxTransactionChannelType.id;
}
