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
import { IShareholderType, getShareholderTypeIdentifier } from '../shareholder-type.model';

export type EntityResponseType = HttpResponse<IShareholderType>;
export type EntityArrayResponseType = HttpResponse<IShareholderType[]>;

@Injectable({ providedIn: 'root' })
export class ShareholderTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/shareholder-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/shareholder-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(shareholderType: IShareholderType): Observable<EntityResponseType> {
    return this.http.post<IShareholderType>(this.resourceUrl, shareholderType, { observe: 'response' });
  }

  update(shareholderType: IShareholderType): Observable<EntityResponseType> {
    return this.http.put<IShareholderType>(
      `${this.resourceUrl}/${getShareholderTypeIdentifier(shareholderType) as number}`,
      shareholderType,
      { observe: 'response' }
    );
  }

  partialUpdate(shareholderType: IShareholderType): Observable<EntityResponseType> {
    return this.http.patch<IShareholderType>(
      `${this.resourceUrl}/${getShareholderTypeIdentifier(shareholderType) as number}`,
      shareholderType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IShareholderType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShareholderType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShareholderType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addShareholderTypeToCollectionIfMissing(
    shareholderTypeCollection: IShareholderType[],
    ...shareholderTypesToCheck: (IShareholderType | null | undefined)[]
  ): IShareholderType[] {
    const shareholderTypes: IShareholderType[] = shareholderTypesToCheck.filter(isPresent);
    if (shareholderTypes.length > 0) {
      const shareholderTypeCollectionIdentifiers = shareholderTypeCollection.map(
        shareholderTypeItem => getShareholderTypeIdentifier(shareholderTypeItem)!
      );
      const shareholderTypesToAdd = shareholderTypes.filter(shareholderTypeItem => {
        const shareholderTypeIdentifier = getShareholderTypeIdentifier(shareholderTypeItem);
        if (shareholderTypeIdentifier == null || shareholderTypeCollectionIdentifiers.includes(shareholderTypeIdentifier)) {
          return false;
        }
        shareholderTypeCollectionIdentifiers.push(shareholderTypeIdentifier);
        return true;
      });
      return [...shareholderTypesToAdd, ...shareholderTypeCollection];
    }
    return shareholderTypeCollection;
  }
}
