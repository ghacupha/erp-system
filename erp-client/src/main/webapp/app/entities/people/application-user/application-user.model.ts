import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { ISecurityClearance } from 'app/entities/people/security-clearance/security-clearance.model';
import { IUser } from 'app/entities/user/user.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';

export interface IApplicationUser {
  id?: number;
  designation?: string;
  applicationIdentity?: string;
  organization?: IDealer;
  department?: IDealer;
  securityClearance?: ISecurityClearance;
  systemIdentity?: IUser;
  userProperties?: IUniversallyUniqueMapping[] | null;
  dealerIdentity?: IDealer;
}

export class ApplicationUser implements IApplicationUser {
  constructor(
    public id?: number,
    public designation?: string,
    public applicationIdentity?: string,
    public organization?: IDealer,
    public department?: IDealer,
    public securityClearance?: ISecurityClearance,
    public systemIdentity?: IUser,
    public userProperties?: IUniversallyUniqueMapping[] | null,
    public dealerIdentity?: IDealer
  ) {}
}

export function getApplicationUserIdentifier(applicationUser: IApplicationUser): number | undefined {
  return applicationUser.id;
}
