import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { ITransactionAccountLedger } from 'app/entities/accounting/transaction-account-ledger/transaction-account-ledger.model';
import { transactionAccountPostingTypes } from 'app/entities/enumerations/transaction-account-posting-types.model';

export interface ITransactionAccountCategory {
  id?: number;
  name?: string;
  transactionAccountPostingType?: transactionAccountPostingTypes;
  placeholders?: IPlaceholder[] | null;
  accountLedger?: ITransactionAccountLedger;
}

export class TransactionAccountCategory implements ITransactionAccountCategory {
  constructor(
    public id?: number,
    public name?: string,
    public transactionAccountPostingType?: transactionAccountPostingTypes,
    public placeholders?: IPlaceholder[] | null,
    public accountLedger?: ITransactionAccountLedger
  ) {}
}

export function getTransactionAccountCategoryIdentifier(transactionAccountCategory: ITransactionAccountCategory): number | undefined {
  return transactionAccountCategory.id;
}
