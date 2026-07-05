export interface IStaffCurrentEmploymentStatus {
  id?: number;
  staffCurrentEmploymentStatusTypeCode?: string;
  staffCurrentEmploymentStatusType?: string;
  staffCurrentEmploymentStatusTypeDetails?: string | null;
}

export class StaffCurrentEmploymentStatus implements IStaffCurrentEmploymentStatus {
  constructor(
    public id?: number,
    public staffCurrentEmploymentStatusTypeCode?: string,
    public staffCurrentEmploymentStatusType?: string,
    public staffCurrentEmploymentStatusTypeDetails?: string | null
  ) {}
}

export function getStaffCurrentEmploymentStatusIdentifier(staffCurrentEmploymentStatus: IStaffCurrentEmploymentStatus): number | undefined {
  return staffCurrentEmploymentStatus.id;
}
