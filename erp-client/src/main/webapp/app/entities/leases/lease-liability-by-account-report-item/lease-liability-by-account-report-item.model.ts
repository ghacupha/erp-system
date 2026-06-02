export interface ILeaseLiabilityByAccountReportItem {
  id?: number;
  accountName?: string | null;
  accountNumber?: string | null;
  description?: string | null;
  accountBalance?: number | null;
}

export class LeaseLiabilityByAccountReportItem implements ILeaseLiabilityByAccountReportItem {
  constructor(
    public id?: number,
    public accountName?: string | null,
    public accountNumber?: string | null,
    public description?: string | null,
    public accountBalance?: number | null
  ) {}
}

export function getLeaseLiabilityByAccountReportItemIdentifier(
  leaseLiabilityByAccountReportItem: ILeaseLiabilityByAccountReportItem
): number | undefined {
  return leaseLiabilityByAccountReportItem.id;
}
