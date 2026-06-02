export interface IEmploymentTerms {
  id?: number;
  employmentTermsCode?: string;
  employmentTermsStatus?: string;
}

export class EmploymentTerms implements IEmploymentTerms {
  constructor(public id?: number, public employmentTermsCode?: string, public employmentTermsStatus?: string) {}
}

export function getEmploymentTermsIdentifier(employmentTerms: IEmploymentTerms): number | undefined {
  return employmentTerms.id;
}
