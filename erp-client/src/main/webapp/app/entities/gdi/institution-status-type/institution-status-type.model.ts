export interface IInstitutionStatusType {
  id?: number;
  institutionStatusCode?: string;
  institutionStatusType?: string | null;
  insitutionStatusTypeDescription?: string | null;
}

export class InstitutionStatusType implements IInstitutionStatusType {
  constructor(
    public id?: number,
    public institutionStatusCode?: string,
    public institutionStatusType?: string | null,
    public insitutionStatusTypeDescription?: string | null
  ) {}
}

export function getInstitutionStatusTypeIdentifier(institutionStatusType: IInstitutionStatusType): number | undefined {
  return institutionStatusType.id;
}
