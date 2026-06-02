export interface ICrbCreditFacilityType {
  id?: number;
  creditFacilityTypeCode?: string;
  creditFacilityType?: string;
  creditFacilityDescription?: string | null;
}

export class CrbCreditFacilityType implements ICrbCreditFacilityType {
  constructor(
    public id?: number,
    public creditFacilityTypeCode?: string,
    public creditFacilityType?: string,
    public creditFacilityDescription?: string | null
  ) {}
}

export function getCrbCreditFacilityTypeIdentifier(crbCreditFacilityType: ICrbCreditFacilityType): number | undefined {
  return crbCreditFacilityType.id;
}
