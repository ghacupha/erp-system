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
import { IAnticipatedMaturityPeriood, getAnticipatedMaturityPerioodIdentifier } from '../anticipated-maturity-periood.model';

export type EntityResponseType = HttpResponse<IAnticipatedMaturityPeriood>;
export type EntityArrayResponseType = HttpResponse<IAnticipatedMaturityPeriood[]>;

@Injectable({ providedIn: 'root' })
export class AnticipatedMaturityPerioodService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/anticipated-maturity-perioods');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/anticipated-maturity-perioods');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(anticipatedMaturityPeriood: IAnticipatedMaturityPeriood): Observable<EntityResponseType> {
    return this.http.post<IAnticipatedMaturityPeriood>(this.resourceUrl, anticipatedMaturityPeriood, { observe: 'response' });
  }

  update(anticipatedMaturityPeriood: IAnticipatedMaturityPeriood): Observable<EntityResponseType> {
    return this.http.put<IAnticipatedMaturityPeriood>(
      `${this.resourceUrl}/${getAnticipatedMaturityPerioodIdentifier(anticipatedMaturityPeriood) as number}`,
      anticipatedMaturityPeriood,
      { observe: 'response' }
    );
  }

  partialUpdate(anticipatedMaturityPeriood: IAnticipatedMaturityPeriood): Observable<EntityResponseType> {
    return this.http.patch<IAnticipatedMaturityPeriood>(
      `${this.resourceUrl}/${getAnticipatedMaturityPerioodIdentifier(anticipatedMaturityPeriood) as number}`,
      anticipatedMaturityPeriood,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAnticipatedMaturityPeriood>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAnticipatedMaturityPeriood[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAnticipatedMaturityPeriood[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addAnticipatedMaturityPerioodToCollectionIfMissing(
    anticipatedMaturityPerioodCollection: IAnticipatedMaturityPeriood[],
    ...anticipatedMaturityPerioodsToCheck: (IAnticipatedMaturityPeriood | null | undefined)[]
  ): IAnticipatedMaturityPeriood[] {
    const anticipatedMaturityPerioods: IAnticipatedMaturityPeriood[] = anticipatedMaturityPerioodsToCheck.filter(isPresent);
    if (anticipatedMaturityPerioods.length > 0) {
      const anticipatedMaturityPerioodCollectionIdentifiers = anticipatedMaturityPerioodCollection.map(
        anticipatedMaturityPerioodItem => getAnticipatedMaturityPerioodIdentifier(anticipatedMaturityPerioodItem)!
      );
      const anticipatedMaturityPerioodsToAdd = anticipatedMaturityPerioods.filter(anticipatedMaturityPerioodItem => {
        const anticipatedMaturityPerioodIdentifier = getAnticipatedMaturityPerioodIdentifier(anticipatedMaturityPerioodItem);
        if (
          anticipatedMaturityPerioodIdentifier == null ||
          anticipatedMaturityPerioodCollectionIdentifiers.includes(anticipatedMaturityPerioodIdentifier)
        ) {
          return false;
        }
        anticipatedMaturityPerioodCollectionIdentifiers.push(anticipatedMaturityPerioodIdentifier);
        return true;
      });
      return [...anticipatedMaturityPerioodsToAdd, ...anticipatedMaturityPerioodCollection];
    }
    return anticipatedMaturityPerioodCollection;
  }
}
