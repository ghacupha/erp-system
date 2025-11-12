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
import { IFraudCategoryFlag, getFraudCategoryFlagIdentifier } from '../fraud-category-flag.model';

export type EntityResponseType = HttpResponse<IFraudCategoryFlag>;
export type EntityArrayResponseType = HttpResponse<IFraudCategoryFlag[]>;

@Injectable({ providedIn: 'root' })
export class FraudCategoryFlagService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fraud-category-flags');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/fraud-category-flags');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fraudCategoryFlag: IFraudCategoryFlag): Observable<EntityResponseType> {
    return this.http.post<IFraudCategoryFlag>(this.resourceUrl, fraudCategoryFlag, { observe: 'response' });
  }

  update(fraudCategoryFlag: IFraudCategoryFlag): Observable<EntityResponseType> {
    return this.http.put<IFraudCategoryFlag>(
      `${this.resourceUrl}/${getFraudCategoryFlagIdentifier(fraudCategoryFlag) as number}`,
      fraudCategoryFlag,
      { observe: 'response' }
    );
  }

  partialUpdate(fraudCategoryFlag: IFraudCategoryFlag): Observable<EntityResponseType> {
    return this.http.patch<IFraudCategoryFlag>(
      `${this.resourceUrl}/${getFraudCategoryFlagIdentifier(fraudCategoryFlag) as number}`,
      fraudCategoryFlag,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFraudCategoryFlag>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFraudCategoryFlag[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFraudCategoryFlag[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addFraudCategoryFlagToCollectionIfMissing(
    fraudCategoryFlagCollection: IFraudCategoryFlag[],
    ...fraudCategoryFlagsToCheck: (IFraudCategoryFlag | null | undefined)[]
  ): IFraudCategoryFlag[] {
    const fraudCategoryFlags: IFraudCategoryFlag[] = fraudCategoryFlagsToCheck.filter(isPresent);
    if (fraudCategoryFlags.length > 0) {
      const fraudCategoryFlagCollectionIdentifiers = fraudCategoryFlagCollection.map(
        fraudCategoryFlagItem => getFraudCategoryFlagIdentifier(fraudCategoryFlagItem)!
      );
      const fraudCategoryFlagsToAdd = fraudCategoryFlags.filter(fraudCategoryFlagItem => {
        const fraudCategoryFlagIdentifier = getFraudCategoryFlagIdentifier(fraudCategoryFlagItem);
        if (fraudCategoryFlagIdentifier == null || fraudCategoryFlagCollectionIdentifiers.includes(fraudCategoryFlagIdentifier)) {
          return false;
        }
        fraudCategoryFlagCollectionIdentifiers.push(fraudCategoryFlagIdentifier);
        return true;
      });
      return [...fraudCategoryFlagsToAdd, ...fraudCategoryFlagCollection];
    }
    return fraudCategoryFlagCollection;
  }
}
