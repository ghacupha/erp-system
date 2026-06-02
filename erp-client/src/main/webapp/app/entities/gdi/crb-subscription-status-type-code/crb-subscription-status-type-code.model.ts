export interface ICrbSubscriptionStatusTypeCode {
  id?: number;
  subscriptionStatusTypeCode?: string;
  subscriptionStatusType?: string;
  subscriptionStatusTypeDescription?: string | null;
}

export class CrbSubscriptionStatusTypeCode implements ICrbSubscriptionStatusTypeCode {
  constructor(
    public id?: number,
    public subscriptionStatusTypeCode?: string,
    public subscriptionStatusType?: string,
    public subscriptionStatusTypeDescription?: string | null
  ) {}
}

export function getCrbSubscriptionStatusTypeCodeIdentifier(
  crbSubscriptionStatusTypeCode: ICrbSubscriptionStatusTypeCode
): number | undefined {
  return crbSubscriptionStatusTypeCode.id;
}
