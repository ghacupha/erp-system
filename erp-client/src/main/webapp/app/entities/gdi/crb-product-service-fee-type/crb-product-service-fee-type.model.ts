export interface ICrbProductServiceFeeType {
  id?: number;
  chargeTypeCode?: string;
  chargeTypeDescription?: string | null;
  chargeTypeCategory?: string;
}

export class CrbProductServiceFeeType implements ICrbProductServiceFeeType {
  constructor(
    public id?: number,
    public chargeTypeCode?: string,
    public chargeTypeDescription?: string | null,
    public chargeTypeCategory?: string
  ) {}
}

export function getCrbProductServiceFeeTypeIdentifier(crbProductServiceFeeType: ICrbProductServiceFeeType): number | undefined {
  return crbProductServiceFeeType.id;
}
