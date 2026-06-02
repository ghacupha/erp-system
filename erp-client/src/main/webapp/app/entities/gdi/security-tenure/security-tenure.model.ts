export interface ISecurityTenure {
  id?: number;
  securityTenureCode?: string;
  securityTenureType?: string;
  securityTenureDetails?: string | null;
}

export class SecurityTenure implements ISecurityTenure {
  constructor(
    public id?: number,
    public securityTenureCode?: string,
    public securityTenureType?: string,
    public securityTenureDetails?: string | null
  ) {}
}

export function getSecurityTenureIdentifier(securityTenure: ISecurityTenure): number | undefined {
  return securityTenure.id;
}
