export interface ILeaseLiabilityPostingReportItem {
  id?: number;
  bookingId?: string | null;
  leaseTitle?: string | null;
  leaseDescription?: string | null;
  accountNumber?: string | null;
  posting?: string | null;
  postingAmount?: number | null;
}

export class LeaseLiabilityPostingReportItem implements ILeaseLiabilityPostingReportItem {
  constructor(
    public id?: number,
    public bookingId?: string | null,
    public leaseTitle?: string | null,
    public leaseDescription?: string | null,
    public accountNumber?: string | null,
    public posting?: string | null,
    public postingAmount?: number | null
  ) {}
}

export function getLeaseLiabilityPostingReportItemIdentifier(
  leaseLiabilityPostingReportItem: ILeaseLiabilityPostingReportItem
): number | undefined {
  return leaseLiabilityPostingReportItem.id;
}
