export interface ILoanRepaymentFrequency {
  id?: number;
  frequencyTypeCode?: string;
  frequencyType?: string;
  frequencyTypeDetails?: string | null;
}

export class LoanRepaymentFrequency implements ILoanRepaymentFrequency {
  constructor(
    public id?: number,
    public frequencyTypeCode?: string,
    public frequencyType?: string,
    public frequencyTypeDetails?: string | null
  ) {}
}

export function getLoanRepaymentFrequencyIdentifier(loanRepaymentFrequency: ILoanRepaymentFrequency): number | undefined {
  return loanRepaymentFrequency.id;
}
