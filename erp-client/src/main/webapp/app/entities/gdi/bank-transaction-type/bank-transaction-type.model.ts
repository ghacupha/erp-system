export interface IBankTransactionType {
  id?: number;
  transactionTypeCode?: string;
  transactionTypeDetails?: string;
}

export class BankTransactionType implements IBankTransactionType {
  constructor(public id?: number, public transactionTypeCode?: string, public transactionTypeDetails?: string) {}
}

export function getBankTransactionTypeIdentifier(bankTransactionType: IBankTransactionType): number | undefined {
  return bankTransactionType.id;
}
