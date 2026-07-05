export interface IAccountOwnershipType {
  id?: number;
  accountOwnershipTypeCode?: string;
  accountOwnershipType?: string;
  description?: string | null;
}

export class AccountOwnershipType implements IAccountOwnershipType {
  constructor(
    public id?: number,
    public accountOwnershipTypeCode?: string,
    public accountOwnershipType?: string,
    public description?: string | null
  ) {}
}

export function getAccountOwnershipTypeIdentifier(accountOwnershipType: IAccountOwnershipType): number | undefined {
  return accountOwnershipType.id;
}
