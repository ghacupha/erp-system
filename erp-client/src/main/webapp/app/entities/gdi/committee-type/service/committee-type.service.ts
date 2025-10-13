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
import { ICommitteeType, getCommitteeTypeIdentifier } from '../committee-type.model';

export type EntityResponseType = HttpResponse<ICommitteeType>;
export type EntityArrayResponseType = HttpResponse<ICommitteeType[]>;

@Injectable({ providedIn: 'root' })
export class CommitteeTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/committee-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/committee-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(committeeType: ICommitteeType): Observable<EntityResponseType> {
    return this.http.post<ICommitteeType>(this.resourceUrl, committeeType, { observe: 'response' });
  }

  update(committeeType: ICommitteeType): Observable<EntityResponseType> {
    return this.http.put<ICommitteeType>(`${this.resourceUrl}/${getCommitteeTypeIdentifier(committeeType) as number}`, committeeType, {
      observe: 'response',
    });
  }

  partialUpdate(committeeType: ICommitteeType): Observable<EntityResponseType> {
    return this.http.patch<ICommitteeType>(`${this.resourceUrl}/${getCommitteeTypeIdentifier(committeeType) as number}`, committeeType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICommitteeType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommitteeType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommitteeType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCommitteeTypeToCollectionIfMissing(
    committeeTypeCollection: ICommitteeType[],
    ...committeeTypesToCheck: (ICommitteeType | null | undefined)[]
  ): ICommitteeType[] {
    const committeeTypes: ICommitteeType[] = committeeTypesToCheck.filter(isPresent);
    if (committeeTypes.length > 0) {
      const committeeTypeCollectionIdentifiers = committeeTypeCollection.map(
        committeeTypeItem => getCommitteeTypeIdentifier(committeeTypeItem)!
      );
      const committeeTypesToAdd = committeeTypes.filter(committeeTypeItem => {
        const committeeTypeIdentifier = getCommitteeTypeIdentifier(committeeTypeItem);
        if (committeeTypeIdentifier == null || committeeTypeCollectionIdentifiers.includes(committeeTypeIdentifier)) {
          return false;
        }
        committeeTypeCollectionIdentifiers.push(committeeTypeIdentifier);
        return true;
      });
      return [...committeeTypesToAdd, ...committeeTypeCollection];
    }
    return committeeTypeCollection;
  }
}
