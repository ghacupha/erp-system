import { ILeaseLiability } from 'app/entities/leases/lease-liability/lease-liability.model';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';

export interface ILeaseAmortizationCalculation {
  id?: number;
  interestRate?: number | null;
  periodicity?: string | null;
  leaseAmount?: number | null;
  numberOfPeriods?: number | null;
  leaseLiability?: ILeaseLiability | null;
  leaseContract?: IIFRS16LeaseContract;
}

export class LeaseAmortizationCalculation implements ILeaseAmortizationCalculation {
  constructor(
    public id?: number,
    public interestRate?: number | null,
    public periodicity?: string | null,
    public leaseAmount?: number | null,
    public numberOfPeriods?: number | null,
    public leaseLiability?: ILeaseLiability | null,
    public leaseContract?: IIFRS16LeaseContract
  ) {}
}

export function getLeaseAmortizationCalculationIdentifier(leaseAmortizationCalculation: ILeaseAmortizationCalculation): number | undefined {
  return leaseAmortizationCalculation.id;
}
