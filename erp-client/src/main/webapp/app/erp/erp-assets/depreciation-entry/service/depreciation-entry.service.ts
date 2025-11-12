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
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDepreciationEntry, getDepreciationEntryIdentifier } from '../depreciation-entry.model';

export type EntityResponseType = HttpResponse<IDepreciationEntry>;
export type EntityArrayResponseType = HttpResponse<IDepreciationEntry[]>;

@Injectable({ providedIn: 'root' })
export class DepreciationEntryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/depreciation-entries');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/depreciation-entries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(depreciationEntry: IDepreciationEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depreciationEntry);
    return this.http
      .post<IDepreciationEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(depreciationEntry: IDepreciationEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depreciationEntry);
    return this.http
      .put<IDepreciationEntry>(`${this.resourceUrl}/${getDepreciationEntryIdentifier(depreciationEntry) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(depreciationEntry: IDepreciationEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depreciationEntry);
    return this.http
      .patch<IDepreciationEntry>(`${this.resourceUrl}/${getDepreciationEntryIdentifier(depreciationEntry) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDepreciationEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDepreciationEntry[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDepreciationEntry[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addDepreciationEntryToCollectionIfMissing(
    depreciationEntryCollection: IDepreciationEntry[],
    ...depreciationEntriesToCheck: (IDepreciationEntry | null | undefined)[]
  ): IDepreciationEntry[] {
    const depreciationEntries: IDepreciationEntry[] = depreciationEntriesToCheck.filter(isPresent);
    if (depreciationEntries.length > 0) {
      const depreciationEntryCollectionIdentifiers = depreciationEntryCollection.map(
        depreciationEntryItem => getDepreciationEntryIdentifier(depreciationEntryItem)!
      );
      const depreciationEntriesToAdd = depreciationEntries.filter(depreciationEntryItem => {
        const depreciationEntryIdentifier = getDepreciationEntryIdentifier(depreciationEntryItem);
        if (depreciationEntryIdentifier == null || depreciationEntryCollectionIdentifiers.includes(depreciationEntryIdentifier)) {
          return false;
        }
        depreciationEntryCollectionIdentifiers.push(depreciationEntryIdentifier);
        return true;
      });
      return [...depreciationEntriesToAdd, ...depreciationEntryCollection];
    }
    return depreciationEntryCollection;
  }

  protected convertDateFromClient(depreciationEntry: IDepreciationEntry): IDepreciationEntry {
    return Object.assign({}, depreciationEntry, {
      postedAt: depreciationEntry.postedAt?.isValid() ? depreciationEntry.postedAt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.postedAt = res.body.postedAt ? dayjs(res.body.postedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((depreciationEntry: IDepreciationEntry) => {
        depreciationEntry.postedAt = depreciationEntry.postedAt ? dayjs(depreciationEntry.postedAt) : undefined;
      });
    }
    return res;
  }
}
