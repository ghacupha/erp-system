///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
import { ISecurityClearance, getSecurityClearanceIdentifier } from '../security-clearance.model';

export type EntityResponseType = HttpResponse<ISecurityClearance>;
export type EntityArrayResponseType = HttpResponse<ISecurityClearance[]>;

@Injectable({ providedIn: 'root' })
export class SecurityClearanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/security-clearances');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/security-clearances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(securityClearance: ISecurityClearance): Observable<EntityResponseType> {
    return this.http.post<ISecurityClearance>(this.resourceUrl, securityClearance, { observe: 'response' });
  }

  update(securityClearance: ISecurityClearance): Observable<EntityResponseType> {
    return this.http.put<ISecurityClearance>(
      `${this.resourceUrl}/${getSecurityClearanceIdentifier(securityClearance) as number}`,
      securityClearance,
      { observe: 'response' }
    );
  }

  partialUpdate(securityClearance: ISecurityClearance): Observable<EntityResponseType> {
    return this.http.patch<ISecurityClearance>(
      `${this.resourceUrl}/${getSecurityClearanceIdentifier(securityClearance) as number}`,
      securityClearance,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISecurityClearance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISecurityClearance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISecurityClearance[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addSecurityClearanceToCollectionIfMissing(
    securityClearanceCollection: ISecurityClearance[],
    ...securityClearancesToCheck: (ISecurityClearance | null | undefined)[]
  ): ISecurityClearance[] {
    const securityClearances: ISecurityClearance[] = securityClearancesToCheck.filter(isPresent);
    if (securityClearances.length > 0) {
      const securityClearanceCollectionIdentifiers = securityClearanceCollection.map(
        securityClearanceItem => getSecurityClearanceIdentifier(securityClearanceItem)!
      );
      const securityClearancesToAdd = securityClearances.filter(securityClearanceItem => {
        const securityClearanceIdentifier = getSecurityClearanceIdentifier(securityClearanceItem);
        if (securityClearanceIdentifier == null || securityClearanceCollectionIdentifiers.includes(securityClearanceIdentifier)) {
          return false;
        }
        securityClearanceCollectionIdentifiers.push(securityClearanceIdentifier);
        return true;
      });
      return [...securityClearancesToAdd, ...securityClearanceCollection];
    }
    return securityClearanceCollection;
  }
}
