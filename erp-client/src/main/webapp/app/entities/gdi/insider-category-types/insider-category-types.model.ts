export interface IInsiderCategoryTypes {
  id?: number;
  insiderCategoryTypeCode?: string;
  insiderCategoryTypeDetail?: string;
  insiderCategoryDescription?: string | null;
}

export class InsiderCategoryTypes implements IInsiderCategoryTypes {
  constructor(
    public id?: number,
    public insiderCategoryTypeCode?: string,
    public insiderCategoryTypeDetail?: string,
    public insiderCategoryDescription?: string | null
  ) {}
}

export function getInsiderCategoryTypesIdentifier(insiderCategoryTypes: IInsiderCategoryTypes): number | undefined {
  return insiderCategoryTypes.id;
}
