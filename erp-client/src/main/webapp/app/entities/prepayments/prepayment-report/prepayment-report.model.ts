import * as dayjs from 'dayjs';

export interface IPrepaymentReport {
  id?: number;
  catalogueNumber?: string | null;
  particulars?: string | null;
  dealerName?: string | null;
  paymentNumber?: string | null;
  paymentDate?: dayjs.Dayjs | null;
  currencyCode?: string | null;
  prepaymentAmount?: number | null;
  amortisedAmount?: number | null;
  outstandingAmount?: number | null;
}

export class PrepaymentReport implements IPrepaymentReport {
  constructor(
    public id?: number,
    public catalogueNumber?: string | null,
    public particulars?: string | null,
    public dealerName?: string | null,
    public paymentNumber?: string | null,
    public paymentDate?: dayjs.Dayjs | null,
    public currencyCode?: string | null,
    public prepaymentAmount?: number | null,
    public amortisedAmount?: number | null,
    public outstandingAmount?: number | null
  ) {}
}

export function getPrepaymentReportIdentifier(prepaymentReport: IPrepaymentReport): number | undefined {
  return prepaymentReport.id;
}
