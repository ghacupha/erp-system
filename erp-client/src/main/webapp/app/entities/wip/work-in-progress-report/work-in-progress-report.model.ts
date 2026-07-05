export interface IWorkInProgressReport {
  id?: number;
  projectTitle?: string | null;
  dealerName?: string | null;
  numberOfItems?: number | null;
  instalmentAmount?: number | null;
  transferAmount?: number | null;
  outstandingAmount?: number | null;
}

export class WorkInProgressReport implements IWorkInProgressReport {
  constructor(
    public id?: number,
    public projectTitle?: string | null,
    public dealerName?: string | null,
    public numberOfItems?: number | null,
    public instalmentAmount?: number | null,
    public transferAmount?: number | null,
    public outstandingAmount?: number | null
  ) {}
}

export function getWorkInProgressReportIdentifier(workInProgressReport: IWorkInProgressReport): number | undefined {
  return workInProgressReport.id;
}
