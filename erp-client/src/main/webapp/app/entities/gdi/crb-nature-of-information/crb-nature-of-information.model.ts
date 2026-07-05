export interface ICrbNatureOfInformation {
  id?: number;
  natureOfInformationTypeCode?: string;
  natureOfInformationType?: string;
  natureOfInformationTypeDescription?: string | null;
}

export class CrbNatureOfInformation implements ICrbNatureOfInformation {
  constructor(
    public id?: number,
    public natureOfInformationTypeCode?: string,
    public natureOfInformationType?: string,
    public natureOfInformationTypeDescription?: string | null
  ) {}
}

export function getCrbNatureOfInformationIdentifier(crbNatureOfInformation: ICrbNatureOfInformation): number | undefined {
  return crbNatureOfInformation.id;
}
