import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface ITARecognitionROURule {
  id?: number;
  name?: string;
  identifier?: string;
  leaseContract?: IIFRS16LeaseContract;
  debit?: ITransactionAccount;
  credit?: ITransactionAccount;
  placeholders?: IPlaceholder[] | null;
}

export class TARecognitionROURule implements ITARecognitionROURule {
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

export function getTARecognitionROURuleIdentifier(tARecognitionROURule: ITARecognitionROURule): number | undefined {
  return tARecognitionROURule.id;
}
