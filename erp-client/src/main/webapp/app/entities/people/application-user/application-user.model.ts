///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

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
