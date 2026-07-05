export interface IStaffRoleType {
  id?: number;
  staffRoleTypeCode?: string;
  staffRoleType?: string;
  staffRoleTypeDetails?: string | null;
}

export class StaffRoleType implements IStaffRoleType {
  constructor(
    public id?: number,
    public staffRoleTypeCode?: string,
    public staffRoleType?: string,
    public staffRoleTypeDetails?: string | null
  ) {}
}

export function getStaffRoleTypeIdentifier(staffRoleType: IStaffRoleType): number | undefined {
  return staffRoleType.id;
}
