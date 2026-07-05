export interface ICrbSubmittingInstitutionCategory {
  id?: number;
  submittingInstitutionCategoryTypeCode?: string;
  submittingInstitutionCategoryType?: string;
  submittingInstitutionCategoryDetails?: string | null;
}

export class CrbSubmittingInstitutionCategory implements ICrbSubmittingInstitutionCategory {
  constructor(
    public id?: number,
    public submittingInstitutionCategoryTypeCode?: string,
    public submittingInstitutionCategoryType?: string,
    public submittingInstitutionCategoryDetails?: string | null
  ) {}
}

export function getCrbSubmittingInstitutionCategoryIdentifier(
  crbSubmittingInstitutionCategory: ICrbSubmittingInstitutionCategory
): number | undefined {
  return crbSubmittingInstitutionCategory.id;
}
