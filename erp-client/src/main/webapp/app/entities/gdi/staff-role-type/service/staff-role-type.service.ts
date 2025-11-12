///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IStaffRoleType, getStaffRoleTypeIdentifier } from '../staff-role-type.model';

export type EntityResponseType = HttpResponse<IStaffRoleType>;
export type EntityArrayResponseType = HttpResponse<IStaffRoleType[]>;

@Injectable({ providedIn: 'root' })
export class StaffRoleTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/staff-role-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/staff-role-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(staffRoleType: IStaffRoleType): Observable<EntityResponseType> {
    return this.http.post<IStaffRoleType>(this.resourceUrl, staffRoleType, { observe: 'response' });
  }

  update(staffRoleType: IStaffRoleType): Observable<EntityResponseType> {
    return this.http.put<IStaffRoleType>(`${this.resourceUrl}/${getStaffRoleTypeIdentifier(staffRoleType) as number}`, staffRoleType, {
      observe: 'response',
    });
  }

  partialUpdate(staffRoleType: IStaffRoleType): Observable<EntityResponseType> {
    return this.http.patch<IStaffRoleType>(`${this.resourceUrl}/${getStaffRoleTypeIdentifier(staffRoleType) as number}`, staffRoleType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStaffRoleType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStaffRoleType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStaffRoleType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addStaffRoleTypeToCollectionIfMissing(
    staffRoleTypeCollection: IStaffRoleType[],
    ...staffRoleTypesToCheck: (IStaffRoleType | null | undefined)[]
  ): IStaffRoleType[] {
    const staffRoleTypes: IStaffRoleType[] = staffRoleTypesToCheck.filter(isPresent);
    if (staffRoleTypes.length > 0) {
      const staffRoleTypeCollectionIdentifiers = staffRoleTypeCollection.map(
        staffRoleTypeItem => getStaffRoleTypeIdentifier(staffRoleTypeItem)!
      );
      const staffRoleTypesToAdd = staffRoleTypes.filter(staffRoleTypeItem => {
        const staffRoleTypeIdentifier = getStaffRoleTypeIdentifier(staffRoleTypeItem);
        if (staffRoleTypeIdentifier == null || staffRoleTypeCollectionIdentifiers.includes(staffRoleTypeIdentifier)) {
          return false;
        }
        staffRoleTypeCollectionIdentifiers.push(staffRoleTypeIdentifier);
        return true;
      });
      return [...staffRoleTypesToAdd, ...staffRoleTypeCollection];
    }
    return staffRoleTypeCollection;
  }
}
