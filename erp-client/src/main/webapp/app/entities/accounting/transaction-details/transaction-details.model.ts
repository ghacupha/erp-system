import * as dayjs from 'dayjs';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';

export interface ITransactionDetails {
  id?: number;
  entryId?: number;
  transactionDate?: dayjs.Dayjs;
  description?: string | null;
  amount?: number;
  createdAt?: dayjs.Dayjs;
  modifiedAt?: dayjs.Dayjs | null;
  transactionType?: string | null;
  debitAccount?: ITransactionAccount;
  creditAccount?: ITransactionAccount;
  placeholders?: IPlaceholder[] | null;
  postedBy?: IApplicationUser | null;
}

export class TransactionDetails implements ITransactionDetails {
  constructor(
    public id?: number,
    public entryId?: number,
    public transactionDate?: dayjs.Dayjs,
    public description?: string | null,
    public amount?: number,
    public createdAt?: dayjs.Dayjs,
    public modifiedAt?: dayjs.Dayjs | null,
    public transactionType?: string | null,
    public debitAccount?: ITransactionAccount,
    public creditAccount?: ITransactionAccount,
    public placeholders?: IPlaceholder[] | null,
    public postedBy?: IApplicationUser | null
  ) {}
}

export function getTransactionDetailsIdentifier(transactionDetails: ITransactionDetails): number | undefined {
  return transactionDetails.id;
}
