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
import { IAlgorithm, getAlgorithmIdentifier } from '../algorithm.model';

export type EntityResponseType = HttpResponse<IAlgorithm>;
export type EntityArrayResponseType = HttpResponse<IAlgorithm[]>;

@Injectable({ providedIn: 'root' })
export class AlgorithmService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/algorithms');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/algorithms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(algorithm: IAlgorithm): Observable<EntityResponseType> {
    return this.http.post<IAlgorithm>(this.resourceUrl, algorithm, { observe: 'response' });
  }

  update(algorithm: IAlgorithm): Observable<EntityResponseType> {
    return this.http.put<IAlgorithm>(`${this.resourceUrl}/${getAlgorithmIdentifier(algorithm) as number}`, algorithm, {
      observe: 'response',
    });
  }

  partialUpdate(algorithm: IAlgorithm): Observable<EntityResponseType> {
    return this.http.patch<IAlgorithm>(`${this.resourceUrl}/${getAlgorithmIdentifier(algorithm) as number}`, algorithm, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAlgorithm>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAlgorithm[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAlgorithm[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addAlgorithmToCollectionIfMissing(
    algorithmCollection: IAlgorithm[],
    ...algorithmsToCheck: (IAlgorithm | null | undefined)[]
  ): IAlgorithm[] {
    const algorithms: IAlgorithm[] = algorithmsToCheck.filter(isPresent);
    if (algorithms.length > 0) {
      const algorithmCollectionIdentifiers = algorithmCollection.map(algorithmItem => getAlgorithmIdentifier(algorithmItem)!);
      const algorithmsToAdd = algorithms.filter(algorithmItem => {
        const algorithmIdentifier = getAlgorithmIdentifier(algorithmItem);
        if (algorithmIdentifier == null || algorithmCollectionIdentifiers.includes(algorithmIdentifier)) {
          return false;
        }
        algorithmCollectionIdentifiers.push(algorithmIdentifier);
        return true;
      });
      return [...algorithmsToAdd, ...algorithmCollection];
    }
    return algorithmCollection;
  }
}
