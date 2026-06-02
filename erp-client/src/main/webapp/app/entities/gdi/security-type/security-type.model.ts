export interface ISecurityType {
  id?: number;
  securityTypeCode?: string;
  securityType?: string;
  securityTypeDetails?: string | null;
  securityTypeDescription?: string | null;
}

export class SecurityType implements ISecurityType {
  constructor(
    public id?: number,
    public securityTypeCode?: string,
    public securityType?: string,
    public securityTypeDetails?: string | null,
    public securityTypeDescription?: string | null
  ) {}
}

export function getSecurityTypeIdentifier(securityType: ISecurityType): number | undefined {
  return securityType.id;
}
