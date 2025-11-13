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
import { ICrbDataSubmittingInstitutions, getCrbDataSubmittingInstitutionsIdentifier } from '../crb-data-submitting-institutions.model';

export type EntityResponseType = HttpResponse<ICrbDataSubmittingInstitutions>;
export type EntityArrayResponseType = HttpResponse<ICrbDataSubmittingInstitutions[]>;

@Injectable({ providedIn: 'root' })
export class CrbDataSubmittingInstitutionsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/crb-data-submitting-institutions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/crb-data-submitting-institutions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(crbDataSubmittingInstitutions: ICrbDataSubmittingInstitutions): Observable<EntityResponseType> {
    return this.http.post<ICrbDataSubmittingInstitutions>(this.resourceUrl, crbDataSubmittingInstitutions, { observe: 'response' });
  }

  update(crbDataSubmittingInstitutions: ICrbDataSubmittingInstitutions): Observable<EntityResponseType> {
    return this.http.put<ICrbDataSubmittingInstitutions>(
      `${this.resourceUrl}/${getCrbDataSubmittingInstitutionsIdentifier(crbDataSubmittingInstitutions) as number}`,
      crbDataSubmittingInstitutions,
      { observe: 'response' }
    );
  }

  partialUpdate(crbDataSubmittingInstitutions: ICrbDataSubmittingInstitutions): Observable<EntityResponseType> {
    return this.http.patch<ICrbDataSubmittingInstitutions>(
      `${this.resourceUrl}/${getCrbDataSubmittingInstitutionsIdentifier(crbDataSubmittingInstitutions) as number}`,
      crbDataSubmittingInstitutions,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICrbDataSubmittingInstitutions>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbDataSubmittingInstitutions[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbDataSubmittingInstitutions[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCrbDataSubmittingInstitutionsToCollectionIfMissing(
    crbDataSubmittingInstitutionsCollection: ICrbDataSubmittingInstitutions[],
    ...crbDataSubmittingInstitutionsToCheck: (ICrbDataSubmittingInstitutions | null | undefined)[]
  ): ICrbDataSubmittingInstitutions[] {
    const crbDataSubmittingInstitutions: ICrbDataSubmittingInstitutions[] = crbDataSubmittingInstitutionsToCheck.filter(isPresent);
    if (crbDataSubmittingInstitutions.length > 0) {
      const crbDataSubmittingInstitutionsCollectionIdentifiers = crbDataSubmittingInstitutionsCollection.map(
        crbDataSubmittingInstitutionsItem => getCrbDataSubmittingInstitutionsIdentifier(crbDataSubmittingInstitutionsItem)!
      );
      const crbDataSubmittingInstitutionsToAdd = crbDataSubmittingInstitutions.filter(crbDataSubmittingInstitutionsItem => {
        const crbDataSubmittingInstitutionsIdentifier = getCrbDataSubmittingInstitutionsIdentifier(crbDataSubmittingInstitutionsItem);
        if (
          crbDataSubmittingInstitutionsIdentifier == null ||
          crbDataSubmittingInstitutionsCollectionIdentifiers.includes(crbDataSubmittingInstitutionsIdentifier)
        ) {
          return false;
        }
        crbDataSubmittingInstitutionsCollectionIdentifiers.push(crbDataSubmittingInstitutionsIdentifier);
        return true;
      });
      return [...crbDataSubmittingInstitutionsToAdd, ...crbDataSubmittingInstitutionsCollection];
    }
    return crbDataSubmittingInstitutionsCollection;
  }
}
