export interface IProfessionalQualification {
  id?: number;
  professionalQualificationsCode?: string;
  professionalQualificationsType?: string;
  professionalQualificationsDetails?: string | null;
}

export class ProfessionalQualification implements IProfessionalQualification {
  constructor(
    public id?: number,
    public professionalQualificationsCode?: string,
    public professionalQualificationsType?: string,
    public professionalQualificationsDetails?: string | null
  ) {}
}

export function getProfessionalQualificationIdentifier(professionalQualification: IProfessionalQualification): number | undefined {
  return professionalQualification.id;
}
