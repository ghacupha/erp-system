export interface ICategoryOfSecurity {
  id?: number;
  categoryOfSecurity?: string;
  categoryOfSecurityDetails?: string;
  categoryOfSecurityDescription?: string | null;
}

export class CategoryOfSecurity implements ICategoryOfSecurity {
  constructor(
    public id?: number,
    public categoryOfSecurity?: string,
    public categoryOfSecurityDetails?: string,
    public categoryOfSecurityDescription?: string | null
  ) {}
}

export function getCategoryOfSecurityIdentifier(categoryOfSecurity: ICategoryOfSecurity): number | undefined {
  return categoryOfSecurity.id;
}
