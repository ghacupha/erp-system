import { SourceOrPurposeOfRemittancFlag } from 'app/entities/enumerations/source-or-purpose-of-remittanc-flag.model';

export interface ISourceRemittancePurposeType {
  id?: number;
  sourceOrPurposeTypeCode?: string;
  sourceOrPurposeOfRemittanceFlag?: SourceOrPurposeOfRemittancFlag;
  sourceOrPurposeOfRemittanceType?: string;
  remittancePurposeTypeDetails?: string | null;
}

export class SourceRemittancePurposeType implements ISourceRemittancePurposeType {
  constructor(
    public id?: number,
    public sourceOrPurposeTypeCode?: string,
    public sourceOrPurposeOfRemittanceFlag?: SourceOrPurposeOfRemittancFlag,
    public sourceOrPurposeOfRemittanceType?: string,
    public remittancePurposeTypeDetails?: string | null
  ) {}
}

export function getSourceRemittancePurposeTypeIdentifier(sourceRemittancePurposeType: ISourceRemittancePurposeType): number | undefined {
  return sourceRemittancePurposeType.id;
}
