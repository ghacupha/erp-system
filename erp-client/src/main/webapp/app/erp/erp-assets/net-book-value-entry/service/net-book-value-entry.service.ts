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
import { INetBookValueEntry, getNetBookValueEntryIdentifier } from '../net-book-value-entry.model';

export type EntityResponseType = HttpResponse<INetBookValueEntry>;
export type EntityArrayResponseType = HttpResponse<INetBookValueEntry[]>;

@Injectable({ providedIn: 'root' })
export class NetBookValueEntryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/net-book-value-entries');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/_search/net-book-value-entries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(netBookValueEntry: INetBookValueEntry): Observable<EntityResponseType> {
    return this.http.post<INetBookValueEntry>(this.resourceUrl, netBookValueEntry, { observe: 'response' });
  }

  update(netBookValueEntry: INetBookValueEntry): Observable<EntityResponseType> {
    return this.http.put<INetBookValueEntry>(
      `${this.resourceUrl}/${getNetBookValueEntryIdentifier(netBookValueEntry) as number}`,
      netBookValueEntry,
      { observe: 'response' }
    );
  }

  partialUpdate(netBookValueEntry: INetBookValueEntry): Observable<EntityResponseType> {
    return this.http.patch<INetBookValueEntry>(
      `${this.resourceUrl}/${getNetBookValueEntryIdentifier(netBookValueEntry) as number}`,
      netBookValueEntry,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INetBookValueEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INetBookValueEntry[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INetBookValueEntry[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addNetBookValueEntryToCollectionIfMissing(
    netBookValueEntryCollection: INetBookValueEntry[],
    ...netBookValueEntriesToCheck: (INetBookValueEntry | null | undefined)[]
  ): INetBookValueEntry[] {
    const netBookValueEntries: INetBookValueEntry[] = netBookValueEntriesToCheck.filter(isPresent);
    if (netBookValueEntries.length > 0) {
      const netBookValueEntryCollectionIdentifiers = netBookValueEntryCollection.map(
        netBookValueEntryItem => getNetBookValueEntryIdentifier(netBookValueEntryItem)!
      );
      const netBookValueEntriesToAdd = netBookValueEntries.filter(netBookValueEntryItem => {
        const netBookValueEntryIdentifier = getNetBookValueEntryIdentifier(netBookValueEntryItem);
        if (netBookValueEntryIdentifier == null || netBookValueEntryCollectionIdentifiers.includes(netBookValueEntryIdentifier)) {
          return false;
        }
        netBookValueEntryCollectionIdentifiers.push(netBookValueEntryIdentifier);
        return true;
      });
      return [...netBookValueEntriesToAdd, ...netBookValueEntryCollection];
    }
    return netBookValueEntryCollection;
  }
}
