export interface ILoanApplicationStatus {
  id?: number;
  loanApplicationStatusTypeCode?: string;
  loanApplicationStatusType?: string;
  loanApplicationStatusDetails?: string | null;
}

export class LoanApplicationStatus implements ILoanApplicationStatus {
  constructor(
    public id?: number,
    public loanApplicationStatusTypeCode?: string,
    public loanApplicationStatusType?: string,
    public loanApplicationStatusDetails?: string | null
  ) {}
}

export function getLoanApplicationStatusIdentifier(loanApplicationStatus: ILoanApplicationStatus): number | undefined {
  return loanApplicationStatus.id;
}
