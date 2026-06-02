export interface ISnaSectorCode {
  id?: number;
  sectorTypeCode?: string;
  mainSectorCode?: string | null;
  mainSectorTypeName?: string | null;
  subSectorCode?: string | null;
  subSectorName?: string | null;
  subSubSectorCode?: string | null;
  subSubSectorName?: string | null;
}

export class SnaSectorCode implements ISnaSectorCode {
  constructor(
    public id?: number,
    public sectorTypeCode?: string,
    public mainSectorCode?: string | null,
    public mainSectorTypeName?: string | null,
    public subSectorCode?: string | null,
    public subSectorName?: string | null,
    public subSubSectorCode?: string | null,
    public subSubSectorName?: string | null
  ) {}
}

export function getSnaSectorCodeIdentifier(snaSectorCode: ISnaSectorCode): number | undefined {
  return snaSectorCode.id;
}
