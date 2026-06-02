import * as dayjs from 'dayjs';

export interface IWorkInProgressOutstandingReport {
  id?: number;
  sequenceNumber?: string | null;
  particulars?: string | null;
  dealerName?: string | null;
  instalmentTransactionNumber?: string | null;
  instalmentTransactionDate?: dayjs.Dayjs | null;
  iso4217Code?: string | null;
  instalmentAmount?: number | null;
  totalTransferAmount?: number | null;
  outstandingAmount?: number | null;
}

export class WorkInProgressOutstandingReport implements IWorkInProgressOutstandingReport {
  constructor(
    public id?: number,
    public sequenceNumber?: string | null,
    public particulars?: string | null,
    public dealerName?: string | null,
    public instalmentTransactionNumber?: string | null,
    public instalmentTransactionDate?: dayjs.Dayjs | null,
    public iso4217Code?: string | null,
    public instalmentAmount?: number | null,
    public totalTransferAmount?: number | null,
    public outstandingAmount?: number | null
  ) {}
}

export function getWorkInProgressOutstandingReportIdentifier(
  workInProgressOutstandingReport: IWorkInProgressOutstandingReport
): number | undefined {
  return workInProgressOutstandingReport.id;
}
