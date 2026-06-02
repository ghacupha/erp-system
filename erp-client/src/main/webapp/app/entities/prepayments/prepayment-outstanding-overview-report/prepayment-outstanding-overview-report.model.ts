export interface IPrepaymentOutstandingOverviewReport {
  id?: number;
  totalPrepaymentAmount?: number | null;
  totalAmortisedAmount?: number | null;
  totalOutstandingAmount?: number | null;
  numberOfPrepaymentAccounts?: number | null;
}

export class PrepaymentOutstandingOverviewReport implements IPrepaymentOutstandingOverviewReport {
  constructor(
    public id?: number,
    public totalPrepaymentAmount?: number | null,
    public totalAmortisedAmount?: number | null,
    public totalOutstandingAmount?: number | null,
    public numberOfPrepaymentAccounts?: number | null
  ) {}
}

export function getPrepaymentOutstandingOverviewReportIdentifier(
  prepaymentOutstandingOverviewReport: IPrepaymentOutstandingOverviewReport
): number | undefined {
  return prepaymentOutstandingOverviewReport.id;
}
