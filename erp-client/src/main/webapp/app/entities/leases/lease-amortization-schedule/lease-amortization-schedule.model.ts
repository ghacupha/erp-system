import { ILeaseLiability } from 'app/entities/leases/lease-liability/lease-liability.model';
import { ILeaseLiabilityScheduleItem } from 'app/entities/leases/lease-liability-schedule-item/lease-liability-schedule-item.model';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';

export interface ILeaseAmortizationSchedule {
  id?: number;
  identifier?: string;
  leaseLiability?: ILeaseLiability;
  leaseLiabilityScheduleItems?: ILeaseLiabilityScheduleItem[] | null;
  leaseContract?: IIFRS16LeaseContract;
}

export class LeaseAmortizationSchedule implements ILeaseAmortizationSchedule {
  constructor(
    public id?: number,
    public identifier?: string,
    public leaseLiability?: ILeaseLiability,
    public leaseLiabilityScheduleItems?: ILeaseLiabilityScheduleItem[] | null,
    public leaseContract?: IIFRS16LeaseContract
  ) {}
}

export function getLeaseAmortizationScheduleIdentifier(leaseAmortizationSchedule: ILeaseAmortizationSchedule): number | undefined {
  return leaseAmortizationSchedule.id;
}
