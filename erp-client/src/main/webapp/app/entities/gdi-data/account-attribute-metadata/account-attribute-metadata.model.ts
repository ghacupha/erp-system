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

import { IGdiMasterDataIndex } from 'app/entities/gdi/gdi-master-data-index/gdi-master-data-index.model';
import { MandatoryFieldFlagTypes } from 'app/entities/enumerations/mandatory-field-flag-types.model';

export interface IAccountAttributeMetadata {
  id?: number;
  precedence?: number;
  columnName?: string;
  shortName?: string;
  detailedDefinition?: string | null;
  dataType?: string;
  length?: number | null;
  columnIndex?: string | null;
  mandatoryFieldFlag?: MandatoryFieldFlagTypes;
  businessValidation?: string | null;
  technicalValidation?: string | null;
  dbColumnName?: string | null;
  metadataVersion?: number | null;
  standardInputTemplate?: IGdiMasterDataIndex | null;
}

export class AccountAttributeMetadata implements IAccountAttributeMetadata {
  constructor(
    public id?: number,
    public precedence?: number,
    public columnName?: string,
    public shortName?: string,
    public detailedDefinition?: string | null,
    public dataType?: string,
    public length?: number | null,
    public columnIndex?: string | null,
    public mandatoryFieldFlag?: MandatoryFieldFlagTypes,
    public businessValidation?: string | null,
    public technicalValidation?: string | null,
    public dbColumnName?: string | null,
    public metadataVersion?: number | null,
    public standardInputTemplate?: IGdiMasterDataIndex | null
  ) {}
}

export function getAccountAttributeMetadataIdentifier(accountAttributeMetadata: IAccountAttributeMetadata): number | undefined {
  return accountAttributeMetadata.id;
}
