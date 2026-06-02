export interface IDerivativeSubType {
  id?: number;
  financialDerivativeSubTypeCode?: string;
  financialDerivativeSubTye?: string;
  financialDerivativeSubtypeDetails?: string | null;
}

export class DerivativeSubType implements IDerivativeSubType {
  constructor(
    public id?: number,
    public financialDerivativeSubTypeCode?: string,
    public financialDerivativeSubTye?: string,
    public financialDerivativeSubtypeDetails?: string | null
  ) {}
}

export function getDerivativeSubTypeIdentifier(derivativeSubType: IDerivativeSubType): number | undefined {
  return derivativeSubType.id;
}
