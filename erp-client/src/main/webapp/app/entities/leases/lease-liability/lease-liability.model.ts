import * as dayjs from 'dayjs';
import { ILeaseAmortizationCalculation } from 'app/entities/leases/lease-amortization-calculation/lease-amortization-calculation.model';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';

export interface ILeaseLiability {
  id?: number;
  leaseId?: string;
  liabilityAmount?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  interestRate?: number;
  hasBeenFullyAmortised?: boolean;
  leaseAmortizationCalculation?: ILeaseAmortizationCalculation | null;
  leaseContract?: IIFRS16LeaseContract;
}

export class LeaseLiability implements ILeaseLiability {
  constructor(
    public id?: number,
    public leaseId?: string,
    public liabilityAmount?: number,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public interestRate?: number,
    public hasBeenFullyAmortised?: boolean,
    public leaseAmortizationCalculation?: ILeaseAmortizationCalculation | null,
    public leaseContract?: IIFRS16LeaseContract
  ) {
    this.hasBeenFullyAmortised = this.hasBeenFullyAmortised ?? false;
  }
}

export function getLeaseLiabilityIdentifier(leaseLiability: ILeaseLiability): number | undefined {
  return leaseLiability.id;
}
