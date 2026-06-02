export interface IInstitutionContactDetails {
  id?: number;
  entityId?: string;
  entityName?: string;
  contactType?: string;
  contactLevel?: string | null;
  contactValue?: string | null;
  contactName?: string | null;
  contactDesignation?: string | null;
}

export class InstitutionContactDetails implements IInstitutionContactDetails {
  constructor(
    public id?: number,
    public entityId?: string,
    public entityName?: string,
    public contactType?: string,
    public contactLevel?: string | null,
    public contactValue?: string | null,
    public contactName?: string | null,
    public contactDesignation?: string | null
  ) {}
}

export function getInstitutionContactDetailsIdentifier(institutionContactDetails: IInstitutionContactDetails): number | undefined {
  return institutionContactDetails.id;
}
