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
import { IExecutiveCategoryType, getExecutiveCategoryTypeIdentifier } from '../executive-category-type.model';

export type EntityResponseType = HttpResponse<IExecutiveCategoryType>;
export type EntityArrayResponseType = HttpResponse<IExecutiveCategoryType[]>;

@Injectable({ providedIn: 'root' })
export class ExecutiveCategoryTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/executive-category-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/executive-category-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(executiveCategoryType: IExecutiveCategoryType): Observable<EntityResponseType> {
    return this.http.post<IExecutiveCategoryType>(this.resourceUrl, executiveCategoryType, { observe: 'response' });
  }

  update(executiveCategoryType: IExecutiveCategoryType): Observable<EntityResponseType> {
    return this.http.put<IExecutiveCategoryType>(
      `${this.resourceUrl}/${getExecutiveCategoryTypeIdentifier(executiveCategoryType) as number}`,
      executiveCategoryType,
      { observe: 'response' }
    );
  }

  partialUpdate(executiveCategoryType: IExecutiveCategoryType): Observable<EntityResponseType> {
    return this.http.patch<IExecutiveCategoryType>(
      `${this.resourceUrl}/${getExecutiveCategoryTypeIdentifier(executiveCategoryType) as number}`,
      executiveCategoryType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExecutiveCategoryType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExecutiveCategoryType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExecutiveCategoryType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addExecutiveCategoryTypeToCollectionIfMissing(
    executiveCategoryTypeCollection: IExecutiveCategoryType[],
    ...executiveCategoryTypesToCheck: (IExecutiveCategoryType | null | undefined)[]
  ): IExecutiveCategoryType[] {
    const executiveCategoryTypes: IExecutiveCategoryType[] = executiveCategoryTypesToCheck.filter(isPresent);
    if (executiveCategoryTypes.length > 0) {
      const executiveCategoryTypeCollectionIdentifiers = executiveCategoryTypeCollection.map(
        executiveCategoryTypeItem => getExecutiveCategoryTypeIdentifier(executiveCategoryTypeItem)!
      );
      const executiveCategoryTypesToAdd = executiveCategoryTypes.filter(executiveCategoryTypeItem => {
        const executiveCategoryTypeIdentifier = getExecutiveCategoryTypeIdentifier(executiveCategoryTypeItem);
        if (
          executiveCategoryTypeIdentifier == null ||
          executiveCategoryTypeCollectionIdentifiers.includes(executiveCategoryTypeIdentifier)
        ) {
          return false;
        }
        executiveCategoryTypeCollectionIdentifiers.push(executiveCategoryTypeIdentifier);
        return true;
      });
      return [...executiveCategoryTypesToAdd, ...executiveCategoryTypeCollection];
    }
    return executiveCategoryTypeCollection;
  }
}
