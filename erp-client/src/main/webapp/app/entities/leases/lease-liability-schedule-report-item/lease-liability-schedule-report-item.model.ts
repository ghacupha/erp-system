export interface ILeaseLiabilityScheduleReportItem {
  id?: number;
  sequenceNumber?: number | null;
  openingBalance?: number | null;
  cashPayment?: number | null;
  principalPayment?: number | null;
  interestPayment?: number | null;
  outstandingBalance?: number | null;
  interestPayableOpening?: number | null;
  interestAccrued?: number | null;
  interestPayableClosing?: number | null;
  amortizationScheduleId?: number | null;
}

export class LeaseLiabilityScheduleReportItem implements ILeaseLiabilityScheduleReportItem {
  constructor(
    public id?: number,
    public sequenceNumber?: number | null,
    public openingBalance?: number | null,
    public cashPayment?: number | null,
    public principalPayment?: number | null,
    public interestPayment?: number | null,
    public outstandingBalance?: number | null,
    public interestPayableOpening?: number | null,
    public interestAccrued?: number | null,
    public interestPayableClosing?: number | null,
    public amortizationScheduleId?: number | null
  ) {}
}

export function getLeaseLiabilityScheduleReportItemIdentifier(
  leaseLiabilityScheduleReportItem: ILeaseLiabilityScheduleReportItem
): number | undefined {
  return leaseLiabilityScheduleReportItem.id;
}
