import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IDepartmentType {
  id?: number;
  departmentTypeCode?: string;
  departmentType?: string;
  departmentTypeDetails?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class DepartmentType implements IDepartmentType {
  constructor(
    public id?: number,
    public departmentTypeCode?: string,
    public departmentType?: string,
    public departmentTypeDetails?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getDepartmentTypeIdentifier(departmentType: IDepartmentType): number | undefined {
  return departmentType.id;
}
