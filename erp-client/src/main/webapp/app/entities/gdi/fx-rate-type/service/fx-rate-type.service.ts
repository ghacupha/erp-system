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
import { IFxRateType, getFxRateTypeIdentifier } from '../fx-rate-type.model';

export type EntityResponseType = HttpResponse<IFxRateType>;
export type EntityArrayResponseType = HttpResponse<IFxRateType[]>;

@Injectable({ providedIn: 'root' })
export class FxRateTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fx-rate-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/fx-rate-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fxRateType: IFxRateType): Observable<EntityResponseType> {
    return this.http.post<IFxRateType>(this.resourceUrl, fxRateType, { observe: 'response' });
  }

  update(fxRateType: IFxRateType): Observable<EntityResponseType> {
    return this.http.put<IFxRateType>(`${this.resourceUrl}/${getFxRateTypeIdentifier(fxRateType) as number}`, fxRateType, {
      observe: 'response',
    });
  }

  partialUpdate(fxRateType: IFxRateType): Observable<EntityResponseType> {
    return this.http.patch<IFxRateType>(`${this.resourceUrl}/${getFxRateTypeIdentifier(fxRateType) as number}`, fxRateType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFxRateType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFxRateType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFxRateType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addFxRateTypeToCollectionIfMissing(
    fxRateTypeCollection: IFxRateType[],
    ...fxRateTypesToCheck: (IFxRateType | null | undefined)[]
  ): IFxRateType[] {
    const fxRateTypes: IFxRateType[] = fxRateTypesToCheck.filter(isPresent);
    if (fxRateTypes.length > 0) {
      const fxRateTypeCollectionIdentifiers = fxRateTypeCollection.map(fxRateTypeItem => getFxRateTypeIdentifier(fxRateTypeItem)!);
      const fxRateTypesToAdd = fxRateTypes.filter(fxRateTypeItem => {
        const fxRateTypeIdentifier = getFxRateTypeIdentifier(fxRateTypeItem);
        if (fxRateTypeIdentifier == null || fxRateTypeCollectionIdentifiers.includes(fxRateTypeIdentifier)) {
          return false;
        }
        fxRateTypeCollectionIdentifiers.push(fxRateTypeIdentifier);
        return true;
      });
      return [...fxRateTypesToAdd, ...fxRateTypeCollection];
    }
    return fxRateTypeCollection;
  }
}
