export interface ILoanDeclineReason {
  id?: number;
  loanDeclineReasonTypeCode?: string;
  loanDeclineReasonType?: string;
  loanDeclineReasonDetails?: string | null;
}

export class LoanDeclineReason implements ILoanDeclineReason {
  constructor(
    public id?: number,
    public loanDeclineReasonTypeCode?: string,
    public loanDeclineReasonType?: string,
    public loanDeclineReasonDetails?: string | null
  ) {}
}

export function getLoanDeclineReasonIdentifier(loanDeclineReason: ILoanDeclineReason): number | undefined {
  return loanDeclineReason.id;
}
