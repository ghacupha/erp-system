import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface ITALeaseRecognitionRule {
  id?: number;
  name?: string;
  identifier?: string;
  leaseContract?: IIFRS16LeaseContract;
  debit?: ITransactionAccount;
  credit?: ITransactionAccount;
  placeholders?: IPlaceholder[] | null;
}

export class TALeaseRecognitionRule implements ITALeaseRecognitionRule {
  constructor(
    public id?: number,
    public name?: string,
    public identifier?: string,
    public leaseContract?: IIFRS16LeaseContract,
    public debit?: ITransactionAccount,
    public credit?: ITransactionAccount,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getTALeaseRecognitionRuleIdentifier(tALeaseRecognitionRule: ITALeaseRecognitionRule): number | undefined {
  return tALeaseRecognitionRule.id;
}
