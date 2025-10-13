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
import { IOutletStatus, getOutletStatusIdentifier } from '../outlet-status.model';

export type EntityResponseType = HttpResponse<IOutletStatus>;
export type EntityArrayResponseType = HttpResponse<IOutletStatus[]>;

@Injectable({ providedIn: 'root' })
export class OutletStatusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/granular-data/outlet-statuses');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/granular-data/_search/outlet-statuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(outletStatus: IOutletStatus): Observable<EntityResponseType> {
    return this.http.post<IOutletStatus>(this.resourceUrl, outletStatus, { observe: 'response' });
  }

  update(outletStatus: IOutletStatus): Observable<EntityResponseType> {
    return this.http.put<IOutletStatus>(`${this.resourceUrl}/${getOutletStatusIdentifier(outletStatus) as number}`, outletStatus, {
      observe: 'response',
    });
  }

  partialUpdate(outletStatus: IOutletStatus): Observable<EntityResponseType> {
    return this.http.patch<IOutletStatus>(`${this.resourceUrl}/${getOutletStatusIdentifier(outletStatus) as number}`, outletStatus, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOutletStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOutletStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOutletStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addOutletStatusToCollectionIfMissing(
    outletStatusCollection: IOutletStatus[],
    ...outletStatusesToCheck: (IOutletStatus | null | undefined)[]
  ): IOutletStatus[] {
    const outletStatuses: IOutletStatus[] = outletStatusesToCheck.filter(isPresent);
    if (outletStatuses.length > 0) {
      const outletStatusCollectionIdentifiers = outletStatusCollection.map(
        outletStatusItem => getOutletStatusIdentifier(outletStatusItem)!
      );
      const outletStatusesToAdd = outletStatuses.filter(outletStatusItem => {
        const outletStatusIdentifier = getOutletStatusIdentifier(outletStatusItem);
        if (outletStatusIdentifier == null || outletStatusCollectionIdentifiers.includes(outletStatusIdentifier)) {
          return false;
        }
        outletStatusCollectionIdentifiers.push(outletStatusIdentifier);
        return true;
      });
      return [...outletStatusesToAdd, ...outletStatusCollection];
    }
    return outletStatusCollection;
  }
}
