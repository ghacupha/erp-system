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
import { ITARecognitionROURule, getTARecognitionROURuleIdentifier } from '../ta-recognition-rou-rule.model';

export type EntityResponseType = HttpResponse<ITARecognitionROURule>;
export type EntityArrayResponseType = HttpResponse<ITARecognitionROURule[]>;

@Injectable({ providedIn: 'root' })
export class TARecognitionROURuleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/accounts/ta-recognition-rou-rules');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/accounts/_search/ta-recognition-rou-rules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tARecognitionROURule: ITARecognitionROURule): Observable<EntityResponseType> {
    return this.http.post<ITARecognitionROURule>(this.resourceUrl, tARecognitionROURule, { observe: 'response' });
  }

  update(tARecognitionROURule: ITARecognitionROURule): Observable<EntityResponseType> {
    return this.http.put<ITARecognitionROURule>(
      `${this.resourceUrl}/${getTARecognitionROURuleIdentifier(tARecognitionROURule) as number}`,
      tARecognitionROURule,
      { observe: 'response' }
    );
  }

  partialUpdate(tARecognitionROURule: ITARecognitionROURule): Observable<EntityResponseType> {
    return this.http.patch<ITARecognitionROURule>(
      `${this.resourceUrl}/${getTARecognitionROURuleIdentifier(tARecognitionROURule) as number}`,
      tARecognitionROURule,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITARecognitionROURule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITARecognitionROURule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITARecognitionROURule[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addTARecognitionROURuleToCollectionIfMissing(
    tARecognitionROURuleCollection: ITARecognitionROURule[],
    ...tARecognitionROURulesToCheck: (ITARecognitionROURule | null | undefined)[]
  ): ITARecognitionROURule[] {
    const tARecognitionROURules: ITARecognitionROURule[] = tARecognitionROURulesToCheck.filter(isPresent);
    if (tARecognitionROURules.length > 0) {
      const tARecognitionROURuleCollectionIdentifiers = tARecognitionROURuleCollection.map(
        tARecognitionROURuleItem => getTARecognitionROURuleIdentifier(tARecognitionROURuleItem)!
      );
      const tARecognitionROURulesToAdd = tARecognitionROURules.filter(tARecognitionROURuleItem => {
        const tARecognitionROURuleIdentifier = getTARecognitionROURuleIdentifier(tARecognitionROURuleItem);
        if (tARecognitionROURuleIdentifier == null || tARecognitionROURuleCollectionIdentifiers.includes(tARecognitionROURuleIdentifier)) {
          return false;
        }
        tARecognitionROURuleCollectionIdentifiers.push(tARecognitionROURuleIdentifier);
        return true;
      });
      return [...tARecognitionROURulesToAdd, ...tARecognitionROURuleCollection];
    }
    return tARecognitionROURuleCollection;
  }
}
