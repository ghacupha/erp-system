export interface ILoanPerformanceClassification {
  id?: number;
  loanPerformanceClassificationCode?: string;
  loanPerformanceClassificationType?: string;
  commercialBankDescription?: string | null;
  microfinanceDescription?: string | null;
}

export class LoanPerformanceClassification implements ILoanPerformanceClassification {
  constructor(
    public id?: number,
    public loanPerformanceClassificationCode?: string,
    public loanPerformanceClassificationType?: string,
    public commercialBankDescription?: string | null,
    public microfinanceDescription?: string | null
  ) {}
}

export function getLoanPerformanceClassificationIdentifier(
  loanPerformanceClassification: ILoanPerformanceClassification
): number | undefined {
  return loanPerformanceClassification.id;
}
