export interface ICountySubCountyCode {
  id?: number;
  subCountyCode?: string;
  subCountyName?: string;
  countyCode?: string;
  countyName?: string;
}

export class CountySubCountyCode implements ICountySubCountyCode {
  constructor(
    public id?: number,
    public subCountyCode?: string,
    public subCountyName?: string,
    public countyCode?: string,
    public countyName?: string
  ) {}
}

export function getCountySubCountyCodeIdentifier(countySubCountyCode: ICountySubCountyCode): number | undefined {
  return countySubCountyCode.id;
}
