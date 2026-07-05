export interface IFinancialDerivativeTypeCode {
  id?: number;
  financialDerivativeTypeCode?: string;
  financialDerivativeType?: string;
  financialDerivativeTypeDetails?: string | null;
}

export class FinancialDerivativeTypeCode implements IFinancialDerivativeTypeCode {
  constructor(
    public id?: number,
    public financialDerivativeTypeCode?: string,
    public financialDerivativeType?: string,
    public financialDerivativeTypeDetails?: string | null
  ) {}
}

export function getFinancialDerivativeTypeCodeIdentifier(financialDerivativeTypeCode: IFinancialDerivativeTypeCode): number | undefined {
  return financialDerivativeTypeCode.id;
}
