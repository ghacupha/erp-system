export interface ICollateralType {
  id?: number;
  collateralTypeCode?: string;
  collateralType?: string;
  collateralTypeDescription?: string | null;
}

export class CollateralType implements ICollateralType {
  constructor(
    public id?: number,
    public collateralTypeCode?: string,
    public collateralType?: string,
    public collateralTypeDescription?: string | null
  ) {}
}

export function getCollateralTypeIdentifier(collateralType: ICollateralType): number | undefined {
  return collateralType.id;
}
