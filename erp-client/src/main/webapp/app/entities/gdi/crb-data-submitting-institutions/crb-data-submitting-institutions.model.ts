export interface ICrbDataSubmittingInstitutions {
  id?: number;
  institutionCode?: string;
  institutionName?: string;
  institutionCategory?: string;
}

export class CrbDataSubmittingInstitutions implements ICrbDataSubmittingInstitutions {
  constructor(public id?: number, public institutionCode?: string, public institutionName?: string, public institutionCategory?: string) {}
}

export function getCrbDataSubmittingInstitutionsIdentifier(
  crbDataSubmittingInstitutions: ICrbDataSubmittingInstitutions
): number | undefined {
  return crbDataSubmittingInstitutions.id;
}
