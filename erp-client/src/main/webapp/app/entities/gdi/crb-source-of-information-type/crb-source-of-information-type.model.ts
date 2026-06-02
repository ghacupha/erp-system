export interface ICrbSourceOfInformationType {
  id?: number;
  sourceOfInformationTypeCode?: string;
  sourceOfInformationTypeDescription?: string | null;
}

export class CrbSourceOfInformationType implements ICrbSourceOfInformationType {
  constructor(public id?: number, public sourceOfInformationTypeCode?: string, public sourceOfInformationTypeDescription?: string | null) {}
}

export function getCrbSourceOfInformationTypeIdentifier(crbSourceOfInformationType: ICrbSourceOfInformationType): number | undefined {
  return crbSourceOfInformationType.id;
}
