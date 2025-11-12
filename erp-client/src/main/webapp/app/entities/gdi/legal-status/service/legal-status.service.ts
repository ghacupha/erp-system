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
import { ILegalStatus, getLegalStatusIdentifier } from '../legal-status.model';

export type EntityResponseType = HttpResponse<ILegalStatus>;
export type EntityArrayResponseType = HttpResponse<ILegalStatus[]>;

@Injectable({ providedIn: 'root' })
export class LegalStatusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/legal-statuses');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/legal-statuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(legalStatus: ILegalStatus): Observable<EntityResponseType> {
    return this.http.post<ILegalStatus>(this.resourceUrl, legalStatus, { observe: 'response' });
  }

  update(legalStatus: ILegalStatus): Observable<EntityResponseType> {
    return this.http.put<ILegalStatus>(`${this.resourceUrl}/${getLegalStatusIdentifier(legalStatus) as number}`, legalStatus, {
      observe: 'response',
    });
  }

  partialUpdate(legalStatus: ILegalStatus): Observable<EntityResponseType> {
    return this.http.patch<ILegalStatus>(`${this.resourceUrl}/${getLegalStatusIdentifier(legalStatus) as number}`, legalStatus, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILegalStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILegalStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILegalStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLegalStatusToCollectionIfMissing(
    legalStatusCollection: ILegalStatus[],
    ...legalStatusesToCheck: (ILegalStatus | null | undefined)[]
  ): ILegalStatus[] {
    const legalStatuses: ILegalStatus[] = legalStatusesToCheck.filter(isPresent);
    if (legalStatuses.length > 0) {
      const legalStatusCollectionIdentifiers = legalStatusCollection.map(legalStatusItem => getLegalStatusIdentifier(legalStatusItem)!);
      const legalStatusesToAdd = legalStatuses.filter(legalStatusItem => {
        const legalStatusIdentifier = getLegalStatusIdentifier(legalStatusItem);
        if (legalStatusIdentifier == null || legalStatusCollectionIdentifiers.includes(legalStatusIdentifier)) {
          return false;
        }
        legalStatusCollectionIdentifiers.push(legalStatusIdentifier);
        return true;
      });
      return [...legalStatusesToAdd, ...legalStatusCollection];
    }
    return legalStatusCollection;
  }
}
