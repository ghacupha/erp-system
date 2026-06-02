export interface ITransactionAccountReportItem {
  id?: number;
  accountName?: string | null;
  accountNumber?: string | null;
  accountBalance?: number | null;
}

export class TransactionAccountReportItem implements ITransactionAccountReportItem {
  constructor(
    public id?: number,
    public accountName?: string | null,
    public accountNumber?: string | null,
    public accountBalance?: number | null
  ) {}
}

export function getTransactionAccountReportItemIdentifier(transactionAccountReportItem: ITransactionAccountReportItem): number | undefined {
  return transactionAccountReportItem.id;
}
