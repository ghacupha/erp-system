export interface IAmortizationPostingReport {
  id?: number;
  catalogueNumber?: string | null;
  debitAccount?: string | null;
  creditAccount?: string | null;
  description?: string | null;
  amortizationAmount?: number | null;
}

export class AmortizationPostingReport implements IAmortizationPostingReport {
  constructor(
    public id?: number,
    public catalogueNumber?: string | null,
    public debitAccount?: string | null,
    public creditAccount?: string | null,
    public description?: string | null,
    public amortizationAmount?: number | null
  ) {}
}

export function getAmortizationPostingReportIdentifier(amortizationPostingReport: IAmortizationPostingReport): number | undefined {
  return amortizationPostingReport.id;
}
