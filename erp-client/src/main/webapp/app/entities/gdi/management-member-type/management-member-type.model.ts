export interface IManagementMemberType {
  id?: number;
  managementMemberTypeCode?: string;
  managementMemberType?: string;
}

export class ManagementMemberType implements IManagementMemberType {
  constructor(public id?: number, public managementMemberTypeCode?: string, public managementMemberType?: string) {}
}

export function getManagementMemberTypeIdentifier(managementMemberType: IManagementMemberType): number | undefined {
  return managementMemberType.id;
}
