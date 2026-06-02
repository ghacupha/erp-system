export interface ISecurityClassificationType {
  id?: number;
  securityClassificationTypeCode?: string;
  securityClassificationType?: string;
  securityClassificationDetails?: string | null;
}

export class SecurityClassificationType implements ISecurityClassificationType {
  constructor(
    public id?: number,
    public securityClassificationTypeCode?: string,
    public securityClassificationType?: string,
    public securityClassificationDetails?: string | null
  ) {}
}

export function getSecurityClassificationTypeIdentifier(securityClassificationType: ISecurityClassificationType): number | undefined {
  return securityClassificationType.id;
}
