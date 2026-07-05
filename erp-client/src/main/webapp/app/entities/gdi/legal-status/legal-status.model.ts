export interface ILegalStatus {
  id?: number;
  legalStatusCode?: string;
  legalStatusType?: string;
  legalStatusDescription?: string | null;
}

export class LegalStatus implements ILegalStatus {
  constructor(
    public id?: number,
    public legalStatusCode?: string,
    public legalStatusType?: string,
    public legalStatusDescription?: string | null
  ) {}
}

export function getLegalStatusIdentifier(legalStatus: ILegalStatus): number | undefined {
  return legalStatus.id;
}
