import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { ILeaseAmortizationSchedule } from 'app/entities/leases/lease-amortization-schedule/lease-amortization-schedule.model';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { ILeaseLiability } from 'app/entities/leases/lease-liability/lease-liability.model';
import { ILeaseRepaymentPeriod } from 'app/entities/leases/lease-repayment-period/lease-repayment-period.model';

export interface ILeaseLiabilityScheduleItem {
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
  placeholders?: IPlaceholder[] | null;
  universallyUniqueMappings?: IUniversallyUniqueMapping[] | null;
  leaseAmortizationSchedule?: ILeaseAmortizationSchedule | null;
  leaseContract?: IIFRS16LeaseContract;
  leaseLiability?: ILeaseLiability;
  leasePeriod?: ILeaseRepaymentPeriod;
}

export class LeaseLiabilityScheduleItem implements ILeaseLiabilityScheduleItem {
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
    public placeholders?: IPlaceholder[] | null,
    public universallyUniqueMappings?: IUniversallyUniqueMapping[] | null,
    public leaseAmortizationSchedule?: ILeaseAmortizationSchedule | null,
    public leaseContract?: IIFRS16LeaseContract,
    public leaseLiability?: ILeaseLiability,
    public leasePeriod?: ILeaseRepaymentPeriod
  ) {}
}

export function getLeaseLiabilityScheduleItemIdentifier(leaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem): number | undefined {
  return leaseLiabilityScheduleItem.id;
}
