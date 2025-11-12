///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { taxReferenceTypes } from 'app/entities/enumerations/tax-reference-types.model';

export interface ITaxReference {
  id?: number;
  taxName?: string | null;
  taxDescription?: string | null;
  taxPercentage?: number;
  taxReferenceType?: taxReferenceTypes;
  fileUploadToken?: string | null;
  compilationToken?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class TaxReference implements ITaxReference {
  constructor(
    public id?: number,
    public taxName?: string | null,
    public taxDescription?: string | null,
    public taxPercentage?: number,
    public taxReferenceType?: taxReferenceTypes,
    public fileUploadToken?: string | null,
    public compilationToken?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getTaxReferenceIdentifier(taxReference: ITaxReference): number | undefined {
  return taxReference.id;
}
