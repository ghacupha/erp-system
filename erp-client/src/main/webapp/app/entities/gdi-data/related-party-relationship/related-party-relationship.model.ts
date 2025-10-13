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

import * as dayjs from 'dayjs';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { IPartyRelationType } from 'app/entities/gdi/party-relation-type/party-relation-type.model';

export interface IRelatedPartyRelationship {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  customerId?: string;
  relatedPartyId?: string;
  bankCode?: IInstitutionCode;
  branchId?: IBankBranchCode;
  relationshipType?: IPartyRelationType;
}

export class RelatedPartyRelationship implements IRelatedPartyRelationship {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public customerId?: string,
    public relatedPartyId?: string,
    public bankCode?: IInstitutionCode,
    public branchId?: IBankBranchCode,
    public relationshipType?: IPartyRelationType
  ) {}
}

export function getRelatedPartyRelationshipIdentifier(relatedPartyRelationship: IRelatedPartyRelationship): number | undefined {
  return relatedPartyRelationship.id;
}
