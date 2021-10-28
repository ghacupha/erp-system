import { taxReferenceTypes } from 'app/entities/enumerations/tax-reference-types.model';
import {IPlaceholder} from '../../../../entities/erpService/placeholder/placeholder.model';

export interface ITaxReference {
  id?: number;
  taxName?: string | null;
  taxDescription?: string | null;
  taxPercentage?: number;
  taxReferenceType?: taxReferenceTypes;
  placeholders?: IPlaceholder[] | null;
}

export class TaxReference implements ITaxReference {
  constructor(
    public id?: number,
    public taxName?: string | null,
    public taxDescription?: string | null,
    public taxPercentage?: number,
    public taxReferenceType?: taxReferenceTypes,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getTaxReferenceIdentifier(taxReference: ITaxReference): number | undefined {
  return taxReference.id;
}
