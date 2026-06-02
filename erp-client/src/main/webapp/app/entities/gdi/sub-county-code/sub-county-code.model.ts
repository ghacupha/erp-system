import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface ISubCountyCode {
  id?: number;
  countyCode?: string | null;
  countyName?: string | null;
  subCountyCode?: string | null;
  subCountyName?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class SubCountyCode implements ISubCountyCode {
  constructor(
    public id?: number,
    public countyCode?: string | null,
    public countyName?: string | null,
    public subCountyCode?: string | null,
    public subCountyName?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getSubCountyCodeIdentifier(subCountyCode: ISubCountyCode): number | undefined {
  return subCountyCode.id;
}
