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
import { ICounterPartyCategory, getCounterPartyCategoryIdentifier } from '../counter-party-category.model';

export type EntityResponseType = HttpResponse<ICounterPartyCategory>;
export type EntityArrayResponseType = HttpResponse<ICounterPartyCategory[]>;

@Injectable({ providedIn: 'root' })
export class CounterPartyCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/counter-party-categories');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/counter-party-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(counterPartyCategory: ICounterPartyCategory): Observable<EntityResponseType> {
    return this.http.post<ICounterPartyCategory>(this.resourceUrl, counterPartyCategory, { observe: 'response' });
  }

  update(counterPartyCategory: ICounterPartyCategory): Observable<EntityResponseType> {
    return this.http.put<ICounterPartyCategory>(
      `${this.resourceUrl}/${getCounterPartyCategoryIdentifier(counterPartyCategory) as number}`,
      counterPartyCategory,
      { observe: 'response' }
    );
  }

  partialUpdate(counterPartyCategory: ICounterPartyCategory): Observable<EntityResponseType> {
    return this.http.patch<ICounterPartyCategory>(
      `${this.resourceUrl}/${getCounterPartyCategoryIdentifier(counterPartyCategory) as number}`,
      counterPartyCategory,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICounterPartyCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICounterPartyCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICounterPartyCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCounterPartyCategoryToCollectionIfMissing(
    counterPartyCategoryCollection: ICounterPartyCategory[],
    ...counterPartyCategoriesToCheck: (ICounterPartyCategory | null | undefined)[]
  ): ICounterPartyCategory[] {
    const counterPartyCategories: ICounterPartyCategory[] = counterPartyCategoriesToCheck.filter(isPresent);
    if (counterPartyCategories.length > 0) {
      const counterPartyCategoryCollectionIdentifiers = counterPartyCategoryCollection.map(
        counterPartyCategoryItem => getCounterPartyCategoryIdentifier(counterPartyCategoryItem)!
      );
      const counterPartyCategoriesToAdd = counterPartyCategories.filter(counterPartyCategoryItem => {
        const counterPartyCategoryIdentifier = getCounterPartyCategoryIdentifier(counterPartyCategoryItem);
        if (counterPartyCategoryIdentifier == null || counterPartyCategoryCollectionIdentifiers.includes(counterPartyCategoryIdentifier)) {
          return false;
        }
        counterPartyCategoryCollectionIdentifiers.push(counterPartyCategoryIdentifier);
        return true;
      });
      return [...counterPartyCategoriesToAdd, ...counterPartyCategoryCollection];
    }
    return counterPartyCategoryCollection;
  }
}
