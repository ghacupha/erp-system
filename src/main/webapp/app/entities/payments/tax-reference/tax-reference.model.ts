import { taxReferenceTypes } from 'app/entities/enumerations/tax-reference-types.model';

export interface ITaxReference {
  id?: number;
  taxName?: string | null;
  taxDescription?: string | null;
  taxPercentage?: number;
  taxReferenceType?: taxReferenceTypes;
}

export class TaxReference implements ITaxReference {
  constructor(
    public id?: number,
    public taxName?: string | null,
    public taxDescription?: string | null,
    public taxPercentage?: number,
    public taxReferenceType?: taxReferenceTypes
  ) {}
}

export function getTaxReferenceIdentifier(taxReference: ITaxReference): number | undefined {
  return taxReference.id;
}
