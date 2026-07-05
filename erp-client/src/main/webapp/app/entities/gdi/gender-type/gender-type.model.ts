import { genderTypes } from 'app/entities/enumerations/gender-types.model';

export interface IGenderType {
  id?: number;
  genderCode?: string;
  genderType?: genderTypes;
  genderDescription?: string | null;
}

export class GenderType implements IGenderType {
  constructor(public id?: number, public genderCode?: string, public genderType?: genderTypes, public genderDescription?: string | null) {}
}

export function getGenderTypeIdentifier(genderType: IGenderType): number | undefined {
  return genderType.id;
}
