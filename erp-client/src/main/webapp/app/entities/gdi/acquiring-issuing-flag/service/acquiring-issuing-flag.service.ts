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

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAcquiringIssuingFlag, getAcquiringIssuingFlagIdentifier } from '../acquiring-issuing-flag.model';

export type EntityResponseType = HttpResponse<IAcquiringIssuingFlag>;
export type EntityArrayResponseType = HttpResponse<IAcquiringIssuingFlag[]>;

@Injectable({ providedIn: 'root' })
export class AcquiringIssuingFlagService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/acquiring-issuing-flags');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/acquiring-issuing-flags');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(acquiringIssuingFlag: IAcquiringIssuingFlag): Observable<EntityResponseType> {
    return this.http.post<IAcquiringIssuingFlag>(this.resourceUrl, acquiringIssuingFlag, { observe: 'response' });
  }

  update(acquiringIssuingFlag: IAcquiringIssuingFlag): Observable<EntityResponseType> {
    return this.http.put<IAcquiringIssuingFlag>(
      `${this.resourceUrl}/${getAcquiringIssuingFlagIdentifier(acquiringIssuingFlag) as number}`,
      acquiringIssuingFlag,
      { observe: 'response' }
    );
  }

  partialUpdate(acquiringIssuingFlag: IAcquiringIssuingFlag): Observable<EntityResponseType> {
    return this.http.patch<IAcquiringIssuingFlag>(
      `${this.resourceUrl}/${getAcquiringIssuingFlagIdentifier(acquiringIssuingFlag) as number}`,
      acquiringIssuingFlag,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAcquiringIssuingFlag>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAcquiringIssuingFlag[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAcquiringIssuingFlag[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addAcquiringIssuingFlagToCollectionIfMissing(
    acquiringIssuingFlagCollection: IAcquiringIssuingFlag[],
    ...acquiringIssuingFlagsToCheck: (IAcquiringIssuingFlag | null | undefined)[]
  ): IAcquiringIssuingFlag[] {
    const acquiringIssuingFlags: IAcquiringIssuingFlag[] = acquiringIssuingFlagsToCheck.filter(isPresent);
    if (acquiringIssuingFlags.length > 0) {
      const acquiringIssuingFlagCollectionIdentifiers = acquiringIssuingFlagCollection.map(
        acquiringIssuingFlagItem => getAcquiringIssuingFlagIdentifier(acquiringIssuingFlagItem)!
      );
      const acquiringIssuingFlagsToAdd = acquiringIssuingFlags.filter(acquiringIssuingFlagItem => {
        const acquiringIssuingFlagIdentifier = getAcquiringIssuingFlagIdentifier(acquiringIssuingFlagItem);
        if (acquiringIssuingFlagIdentifier == null || acquiringIssuingFlagCollectionIdentifiers.includes(acquiringIssuingFlagIdentifier)) {
          return false;
        }
        acquiringIssuingFlagCollectionIdentifiers.push(acquiringIssuingFlagIdentifier);
        return true;
      });
      return [...acquiringIssuingFlagsToAdd, ...acquiringIssuingFlagCollection];
    }
    return acquiringIssuingFlagCollection;
  }
}
