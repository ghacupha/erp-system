import { AccountStatusTypes } from 'app/entities/enumerations/account-status-types.model';

export interface IAccountStatusType {
  id?: number;
  accountStatusCode?: string;
  accountStatusType?: AccountStatusTypes;
  accountStatusDescription?: string | null;
}

export class AccountStatusType implements IAccountStatusType {
  constructor(
    public id?: number,
    public accountStatusCode?: string,
    public accountStatusType?: AccountStatusTypes,
    public accountStatusDescription?: string | null
  ) {}
}

export function getAccountStatusTypeIdentifier(accountStatusType: IAccountStatusType): number | undefined {
  return accountStatusType.id;
}
