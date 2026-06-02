import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface ISecurityClearance {
  id?: number;
  clearanceLevel?: string;
  grantedClearances?: ISecurityClearance[] | null;
  placeholders?: IPlaceholder[] | null;
}

export class SecurityClearance implements ISecurityClearance {
  constructor(
    public id?: number,
    public clearanceLevel?: string,
    public grantedClearances?: ISecurityClearance[] | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getSecurityClearanceIdentifier(securityClearance: ISecurityClearance): number | undefined {
  return securityClearance.id;
}
