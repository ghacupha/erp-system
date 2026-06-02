export interface ICrbAccountStatus {
  id?: number;
  accountStatusTypeCode?: string;
  accountStatusType?: string;
  accountStatusTypeDetails?: string | null;
}

export class CrbAccountStatus implements ICrbAccountStatus {
  constructor(
    public id?: number,
    public accountStatusTypeCode?: string,
    public accountStatusType?: string,
    public accountStatusTypeDetails?: string | null
  ) {}
}

export function getCrbAccountStatusIdentifier(crbAccountStatus: ICrbAccountStatus): number | undefined {
  return crbAccountStatus.id;
}
