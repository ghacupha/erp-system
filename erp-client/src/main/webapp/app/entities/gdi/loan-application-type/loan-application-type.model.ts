export interface ILoanApplicationType {
  id?: number;
  loanApplicationTypeCode?: string;
  loanApplicationType?: string;
  loanApplicationDetails?: string | null;
}

export class LoanApplicationType implements ILoanApplicationType {
  constructor(
    public id?: number,
    public loanApplicationTypeCode?: string,
    public loanApplicationType?: string,
    public loanApplicationDetails?: string | null
  ) {}
}

export function getLoanApplicationTypeIdentifier(loanApplicationType: ILoanApplicationType): number | undefined {
  return loanApplicationType.id;
}
