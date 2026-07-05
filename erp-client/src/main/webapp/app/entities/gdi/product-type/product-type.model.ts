export interface IProductType {
  id?: number;
  productCode?: string;
  productType?: string | null;
  productTypeDescription?: string | null;
}

export class ProductType implements IProductType {
  constructor(
    public id?: number,
    public productCode?: string,
    public productType?: string | null,
    public productTypeDescription?: string | null
  ) {}
}

export function getProductTypeIdentifier(productType: IProductType): number | undefined {
  return productType.id;
}
