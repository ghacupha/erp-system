export interface IWorkInProgressOverview {
  id?: number;
  numberOfItems?: number | null;
  instalmentAmount?: number | null;
  totalTransferAmount?: number | null;
  outstandingAmount?: number | null;
}

export class WorkInProgressOverview implements IWorkInProgressOverview {
  constructor(
    public id?: number,
    public numberOfItems?: number | null,
    public instalmentAmount?: number | null,
    public totalTransferAmount?: number | null,
    public outstandingAmount?: number | null
  ) {}
}

export function getWorkInProgressOverviewIdentifier(workInProgressOverview: IWorkInProgressOverview): number | undefined {
  return workInProgressOverview.id;
}
