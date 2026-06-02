export interface IAcademicQualification {
  id?: number;
  academicQualificationsCode?: string;
  academicQualificationType?: string;
  academicQualificationTypeDetail?: string | null;
}

export class AcademicQualification implements IAcademicQualification {
  constructor(
    public id?: number,
    public academicQualificationsCode?: string,
    public academicQualificationType?: string,
    public academicQualificationTypeDetail?: string | null
  ) {}
}

export function getAcademicQualificationIdentifier(academicQualification: IAcademicQualification): number | undefined {
  return academicQualification.id;
}
