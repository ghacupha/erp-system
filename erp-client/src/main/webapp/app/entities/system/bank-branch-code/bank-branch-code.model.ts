import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IBankBranchCode {
  id?: number;
  bankCode?: string | null;
  bankName?: string;
  branchCode?: string;
  branchName?: string | null;
  notes?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class BankBranchCode implements IBankBranchCode {
  constructor(
    public id?: number,
    public bankCode?: string | null,
    public bankName?: string,
    public branchCode?: string,
    public branchName?: string | null,
    public notes?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getBankBranchCodeIdentifier(bankBranchCode: IBankBranchCode): number | undefined {
  return bankBranchCode.id;
}
