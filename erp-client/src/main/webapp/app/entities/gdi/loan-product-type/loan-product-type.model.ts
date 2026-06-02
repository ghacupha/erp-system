export interface ILoanProductType {
  id?: number;
  productCode?: string;
  productType?: string;
  productTypeDescription?: string | null;
}

export class LoanProductType implements ILoanProductType {
  constructor(
    public id?: number,
    public productCode?: string,
    public productType?: string,
    public productTypeDescription?: string | null
  ) {}
}

export function getLoanProductTypeIdentifier(loanProductType: ILoanProductType): number | undefined {
  return loanProductType.id;
}
