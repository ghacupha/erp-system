export interface ICommitteeType {
  id?: number;
  committeeTypeCode?: string;
  committeeType?: string | null;
  committeeTypeDetails?: string | null;
}

export class CommitteeType implements ICommitteeType {
  constructor(
    public id?: number,
    public committeeTypeCode?: string,
    public committeeType?: string | null,
    public committeeTypeDetails?: string | null
  ) {}
}

export function getCommitteeTypeIdentifier(committeeType: ICommitteeType): number | undefined {
  return committeeType.id;
}
