export interface IMerchantType {
  id?: number;
  merchantTypeCode?: string;
  merchantType?: string;
  merchantTypeDetails?: string | null;
}

export class MerchantType implements IMerchantType {
  constructor(
    public id?: number,
    public merchantTypeCode?: string,
    public merchantType?: string,
    public merchantTypeDetails?: string | null
  ) {}
}

export function getMerchantTypeIdentifier(merchantType: IMerchantType): number | undefined {
  return merchantType.id;
}
