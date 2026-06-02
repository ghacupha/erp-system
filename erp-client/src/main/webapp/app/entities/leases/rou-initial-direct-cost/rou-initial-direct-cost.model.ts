import * as dayjs from 'dayjs';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { ISettlement } from 'app/entities/settlement/settlement/settlement.model';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IRouInitialDirectCost {
  id?: number;
  transactionDate?: dayjs.Dayjs;
  description?: string | null;
  cost?: number;
  referenceNumber?: number | null;
  leaseContract?: IIFRS16LeaseContract;
  settlementDetails?: ISettlement;
  targetROUAccount?: ITransactionAccount;
  transferAccount?: ITransactionAccount;
  placeholders?: IPlaceholder[] | null;
}

export class RouInitialDirectCost implements IRouInitialDirectCost {
  constructor(
    public id?: number,
    public transactionDate?: dayjs.Dayjs,
    public description?: string | null,
    public cost?: number,
    public referenceNumber?: number | null,
    public leaseContract?: IIFRS16LeaseContract,
    public settlementDetails?: ISettlement,
    public targetROUAccount?: ITransactionAccount,
    public transferAccount?: ITransactionAccount,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getRouInitialDirectCostIdentifier(rouInitialDirectCost: IRouInitialDirectCost): number | undefined {
  return rouInitialDirectCost.id;
}
