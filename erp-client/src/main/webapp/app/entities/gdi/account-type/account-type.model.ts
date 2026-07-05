export interface IAccountType {
  id?: number;
  accountTypeCode?: string;
  accountType?: string | null;
  description?: string | null;
}

export class AccountType implements IAccountType {
  constructor(
    public id?: number,
    public accountTypeCode?: string,
    public accountType?: string | null,
    public description?: string | null
  ) {}
}

export function getAccountTypeIdentifier(accountType: IAccountType): number | undefined {
  return accountType.id;
}
