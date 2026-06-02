export interface IExecutiveCategoryType {
  id?: number;
  directorCategoryTypeCode?: string;
  directorCategoryType?: string;
  directorCategoryTypeDetails?: string | null;
}

export class ExecutiveCategoryType implements IExecutiveCategoryType {
  constructor(
    public id?: number,
    public directorCategoryTypeCode?: string,
    public directorCategoryType?: string,
    public directorCategoryTypeDetails?: string | null
  ) {}
}

export function getExecutiveCategoryTypeIdentifier(executiveCategoryType: IExecutiveCategoryType): number | undefined {
  return executiveCategoryType.id;
}
