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
import { IPlaceholder, getPlaceholderIdentifier } from '../placeholder.model';

export type EntityResponseType = HttpResponse<IPlaceholder>;
export type EntityArrayResponseType = HttpResponse<IPlaceholder[]>;

@Injectable({ providedIn: 'root' })
export class PlaceholderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/placeholders');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/placeholders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(placeholder: IPlaceholder): Observable<EntityResponseType> {
    return this.http.post<IPlaceholder>(this.resourceUrl, placeholder, { observe: 'response' });
  }

  update(placeholder: IPlaceholder): Observable<EntityResponseType> {
    return this.http.put<IPlaceholder>(`${this.resourceUrl}/${getPlaceholderIdentifier(placeholder) as number}`, placeholder, {
      observe: 'response',
    });
  }

  partialUpdate(placeholder: IPlaceholder): Observable<EntityResponseType> {
    return this.http.patch<IPlaceholder>(`${this.resourceUrl}/${getPlaceholderIdentifier(placeholder) as number}`, placeholder, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlaceholder>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlaceholder[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlaceholder[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addPlaceholderToCollectionIfMissing(
    placeholderCollection: IPlaceholder[],
    ...placeholdersToCheck: (IPlaceholder | null | undefined)[]
  ): IPlaceholder[] {
    const placeholders: IPlaceholder[] = placeholdersToCheck.filter(isPresent);
    if (placeholders.length > 0) {
      const placeholderCollectionIdentifiers = placeholderCollection.map(placeholderItem => getPlaceholderIdentifier(placeholderItem)!);
      const placeholdersToAdd = placeholders.filter(placeholderItem => {
        const placeholderIdentifier = getPlaceholderIdentifier(placeholderItem);
        if (placeholderIdentifier == null || placeholderCollectionIdentifiers.includes(placeholderIdentifier)) {
          return false;
        }
        placeholderCollectionIdentifiers.push(placeholderIdentifier);
        return true;
      });
      return [...placeholdersToAdd, ...placeholderCollection];
    }
    return placeholderCollection;
  }
}
