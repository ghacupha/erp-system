export interface ILeaseLiabilityReportItem {
  id?: number;
  bookingId?: string | null;
  leaseTitle?: string | null;
  liabilityAccountNumber?: string | null;
  liabilityAmount?: number | null;
  interestPayableAccountNumber?: string | null;
  interestPayableAmount?: number | null;
}

export class LeaseLiabilityReportItem implements ILeaseLiabilityReportItem {
  constructor(
    public id?: number,
    public bookingId?: string | null,
    public leaseTitle?: string | null,
    public liabilityAccountNumber?: string | null,
    public liabilityAmount?: number | null,
    public interestPayableAccountNumber?: string | null,
    public interestPayableAmount?: number | null
  ) {}
}

export function getLeaseLiabilityReportItemIdentifier(leaseLiabilityReportItem: ILeaseLiabilityReportItem): number | undefined {
  return leaseLiabilityReportItem.id;
}
