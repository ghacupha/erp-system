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
import { IRemittanceFlag, getRemittanceFlagIdentifier } from '../remittance-flag.model';

export type EntityResponseType = HttpResponse<IRemittanceFlag>;
export type EntityArrayResponseType = HttpResponse<IRemittanceFlag[]>;

@Injectable({ providedIn: 'root' })
export class RemittanceFlagService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/remittance-flags');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/remittance-flags');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(remittanceFlag: IRemittanceFlag): Observable<EntityResponseType> {
    return this.http.post<IRemittanceFlag>(this.resourceUrl, remittanceFlag, { observe: 'response' });
  }

  update(remittanceFlag: IRemittanceFlag): Observable<EntityResponseType> {
    return this.http.put<IRemittanceFlag>(`${this.resourceUrl}/${getRemittanceFlagIdentifier(remittanceFlag) as number}`, remittanceFlag, {
      observe: 'response',
    });
  }

  partialUpdate(remittanceFlag: IRemittanceFlag): Observable<EntityResponseType> {
    return this.http.patch<IRemittanceFlag>(
      `${this.resourceUrl}/${getRemittanceFlagIdentifier(remittanceFlag) as number}`,
      remittanceFlag,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRemittanceFlag>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRemittanceFlag[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRemittanceFlag[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addRemittanceFlagToCollectionIfMissing(
    remittanceFlagCollection: IRemittanceFlag[],
    ...remittanceFlagsToCheck: (IRemittanceFlag | null | undefined)[]
  ): IRemittanceFlag[] {
    const remittanceFlags: IRemittanceFlag[] = remittanceFlagsToCheck.filter(isPresent);
    if (remittanceFlags.length > 0) {
      const remittanceFlagCollectionIdentifiers = remittanceFlagCollection.map(
        remittanceFlagItem => getRemittanceFlagIdentifier(remittanceFlagItem)!
      );
      const remittanceFlagsToAdd = remittanceFlags.filter(remittanceFlagItem => {
        const remittanceFlagIdentifier = getRemittanceFlagIdentifier(remittanceFlagItem);
        if (remittanceFlagIdentifier == null || remittanceFlagCollectionIdentifiers.includes(remittanceFlagIdentifier)) {
          return false;
        }
        remittanceFlagCollectionIdentifiers.push(remittanceFlagIdentifier);
        return true;
      });
      return [...remittanceFlagsToAdd, ...remittanceFlagCollection];
    }
    return remittanceFlagCollection;
  }
}
