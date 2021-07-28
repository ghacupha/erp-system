import { taxReferenceTypes } from 'app/shared/model/enumerations/tax-reference-types.model';

export interface ITaxReference {
  id?: number;
  taxName?: string;
  taxDescription?: string;
  taxPercentage?: number;
  taxReferenceType?: taxReferenceTypes;
}

export class TaxReference implements ITaxReference {
  constructor(
    public id?: number,
    public taxName?: string,
    public taxDescription?: string,
    public taxPercentage?: number,
    public taxReferenceType?: taxReferenceTypes
  ) {}
}
