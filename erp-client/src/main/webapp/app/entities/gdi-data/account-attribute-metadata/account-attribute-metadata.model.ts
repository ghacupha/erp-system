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
