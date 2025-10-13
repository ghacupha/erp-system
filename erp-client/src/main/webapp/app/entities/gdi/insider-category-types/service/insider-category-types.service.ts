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
import { IInsiderCategoryTypes, getInsiderCategoryTypesIdentifier } from '../insider-category-types.model';

export type EntityResponseType = HttpResponse<IInsiderCategoryTypes>;
export type EntityArrayResponseType = HttpResponse<IInsiderCategoryTypes[]>;

@Injectable({ providedIn: 'root' })
export class InsiderCategoryTypesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/insider-category-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/insider-category-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(insiderCategoryTypes: IInsiderCategoryTypes): Observable<EntityResponseType> {
    return this.http.post<IInsiderCategoryTypes>(this.resourceUrl, insiderCategoryTypes, { observe: 'response' });
  }

  update(insiderCategoryTypes: IInsiderCategoryTypes): Observable<EntityResponseType> {
    return this.http.put<IInsiderCategoryTypes>(
      `${this.resourceUrl}/${getInsiderCategoryTypesIdentifier(insiderCategoryTypes) as number}`,
      insiderCategoryTypes,
      { observe: 'response' }
    );
  }

  partialUpdate(insiderCategoryTypes: IInsiderCategoryTypes): Observable<EntityResponseType> {
    return this.http.patch<IInsiderCategoryTypes>(
      `${this.resourceUrl}/${getInsiderCategoryTypesIdentifier(insiderCategoryTypes) as number}`,
      insiderCategoryTypes,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInsiderCategoryTypes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInsiderCategoryTypes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInsiderCategoryTypes[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addInsiderCategoryTypesToCollectionIfMissing(
    insiderCategoryTypesCollection: IInsiderCategoryTypes[],
    ...insiderCategoryTypesToCheck: (IInsiderCategoryTypes | null | undefined)[]
  ): IInsiderCategoryTypes[] {
    const insiderCategoryTypes: IInsiderCategoryTypes[] = insiderCategoryTypesToCheck.filter(isPresent);
    if (insiderCategoryTypes.length > 0) {
      const insiderCategoryTypesCollectionIdentifiers = insiderCategoryTypesCollection.map(
        insiderCategoryTypesItem => getInsiderCategoryTypesIdentifier(insiderCategoryTypesItem)!
      );
      const insiderCategoryTypesToAdd = insiderCategoryTypes.filter(insiderCategoryTypesItem => {
        const insiderCategoryTypesIdentifier = getInsiderCategoryTypesIdentifier(insiderCategoryTypesItem);
        if (insiderCategoryTypesIdentifier == null || insiderCategoryTypesCollectionIdentifiers.includes(insiderCategoryTypesIdentifier)) {
          return false;
        }
        insiderCategoryTypesCollectionIdentifiers.push(insiderCategoryTypesIdentifier);
        return true;
      });
      return [...insiderCategoryTypesToAdd, ...insiderCategoryTypesCollection];
    }
    return insiderCategoryTypesCollection;
  }
}
