export interface IDerivativeUnderlyingAsset {
  id?: number;
  derivativeUnderlyingAssetTypeCode?: string;
  financialDerivativeUnderlyingAssetType?: string;
  derivativeUnderlyingAssetTypeDetails?: string | null;
}

export class DerivativeUnderlyingAsset implements IDerivativeUnderlyingAsset {
  constructor(
    public id?: number,
    public derivativeUnderlyingAssetTypeCode?: string,
    public financialDerivativeUnderlyingAssetType?: string,
    public derivativeUnderlyingAssetTypeDetails?: string | null
  ) {}
}

export function getDerivativeUnderlyingAssetIdentifier(derivativeUnderlyingAsset: IDerivativeUnderlyingAsset): number | undefined {
  return derivativeUnderlyingAsset.id;
}
