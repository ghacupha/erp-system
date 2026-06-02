export interface ICrbCreditApplicationStatus {
  id?: number;
  crbCreditApplicationStatusTypeCode?: string;
  crbCreditApplicationStatusType?: string;
  crbCreditApplicationStatusDetails?: string | null;
}

export class CrbCreditApplicationStatus implements ICrbCreditApplicationStatus {
  constructor(
    public id?: number,
    public crbCreditApplicationStatusTypeCode?: string,
    public crbCreditApplicationStatusType?: string,
    public crbCreditApplicationStatusDetails?: string | null
  ) {}
}

export function getCrbCreditApplicationStatusIdentifier(crbCreditApplicationStatus: ICrbCreditApplicationStatus): number | undefined {
  return crbCreditApplicationStatus.id;
}
