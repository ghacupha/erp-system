export interface IPartyRelationType {
  id?: number;
  partyRelationTypeCode?: string;
  partyRelationType?: string;
  partyRelationTypeDescription?: string | null;
}

export class PartyRelationType implements IPartyRelationType {
  constructor(
    public id?: number,
    public partyRelationTypeCode?: string,
    public partyRelationType?: string,
    public partyRelationTypeDescription?: string | null
  ) {}
}

export function getPartyRelationTypeIdentifier(partyRelationType: IPartyRelationType): number | undefined {
  return partyRelationType.id;
}
