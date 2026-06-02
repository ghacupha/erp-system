import { ITransactionAccountCategory } from 'app/entities/accounting/transaction-account-category/transaction-account-category.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface ITransactionAccountPostingProcessType {
  id?: number;
  name?: string;
  debitAccountType?: ITransactionAccountCategory;
  creditAccountType?: ITransactionAccountCategory;
  placeholders?: IPlaceholder[] | null;
}

export class TransactionAccountPostingProcessType implements ITransactionAccountPostingProcessType {
  constructor(
    public id?: number,
    public name?: string,
    public debitAccountType?: ITransactionAccountCategory,
    public creditAccountType?: ITransactionAccountCategory,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getTransactionAccountPostingProcessTypeIdentifier(
  transactionAccountPostingProcessType: ITransactionAccountPostingProcessType
): number | undefined {
  return transactionAccountPostingProcessType.id;
}
