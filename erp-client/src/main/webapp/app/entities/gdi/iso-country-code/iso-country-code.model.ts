export interface IIsoCountryCode {
  id?: number;
  countryCode?: string | null;
  countryDescription?: string | null;
  continentCode?: string | null;
  continentName?: string | null;
  subRegion?: string | null;
}

export class IsoCountryCode implements IIsoCountryCode {
  constructor(
    public id?: number,
    public countryCode?: string | null,
    public countryDescription?: string | null,
    public continentCode?: string | null,
    public continentName?: string | null,
    public subRegion?: string | null
  ) {}
}

export function getIsoCountryCodeIdentifier(isoCountryCode: IIsoCountryCode): number | undefined {
  return isoCountryCode.id;
}
