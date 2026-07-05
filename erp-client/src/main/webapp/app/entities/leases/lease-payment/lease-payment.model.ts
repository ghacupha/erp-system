import * as dayjs from 'dayjs';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';

export interface ILeasePayment {
  id?: number;
  paymentDate?: dayjs.Dayjs | null;
  paymentAmount?: number | null;
  leaseContract?: IIFRS16LeaseContract;
}

export class LeasePayment implements ILeasePayment {
  constructor(
    public id?: number,
    public paymentDate?: dayjs.Dayjs | null,
    public paymentAmount?: number | null,
    public leaseContract?: IIFRS16LeaseContract
  ) {}
}

export function getLeasePaymentIdentifier(leasePayment: ILeasePayment): number | undefined {
  return leasePayment.id;
}
