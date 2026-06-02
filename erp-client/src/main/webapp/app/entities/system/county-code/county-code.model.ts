import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface ICountyCode {
  id?: number;
  countyCode?: number;
  countyName?: string;
  subCountyCode?: number;
  subCountyName?: string;
  placeholders?: IPlaceholder[] | null;
}

export class CountyCode implements ICountyCode {
  constructor(
    public id?: number,
    public countyCode?: number,
    public countyName?: string,
    public subCountyCode?: number,
    public subCountyName?: string,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getCountyCodeIdentifier(countyCode: ICountyCode): number | undefined {
  return countyCode.id;
}
