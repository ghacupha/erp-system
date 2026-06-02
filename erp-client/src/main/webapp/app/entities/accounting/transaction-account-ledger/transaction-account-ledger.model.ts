import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface ITransactionAccountLedger {
  id?: number;
  ledgerCode?: string;
  ledgerName?: string;
  placeholders?: IPlaceholder[] | null;
}

export class TransactionAccountLedger implements ITransactionAccountLedger {
  constructor(public id?: number, public ledgerCode?: string, public ledgerName?: string, public placeholders?: IPlaceholder[] | null) {}
}

export function getTransactionAccountLedgerIdentifier(transactionAccountLedger: ITransactionAccountLedger): number | undefined {
  return transactionAccountLedger.id;
}
