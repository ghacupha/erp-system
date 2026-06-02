import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IMfbBranchCode {
  id?: number;
  bankCode?: string | null;
  bankName?: string | null;
  branchCode?: string | null;
  branchName?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class MfbBranchCode implements IMfbBranchCode {
  constructor(
    public id?: number,
    public bankCode?: string | null,
    public bankName?: string | null,
    public branchCode?: string | null,
    public branchName?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getMfbBranchCodeIdentifier(mfbBranchCode: IMfbBranchCode): number | undefined {
  return mfbBranchCode.id;
}
