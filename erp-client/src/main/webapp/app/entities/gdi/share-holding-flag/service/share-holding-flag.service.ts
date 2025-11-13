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
import { IShareHoldingFlag, getShareHoldingFlagIdentifier } from '../share-holding-flag.model';

export type EntityResponseType = HttpResponse<IShareHoldingFlag>;
export type EntityArrayResponseType = HttpResponse<IShareHoldingFlag[]>;

@Injectable({ providedIn: 'root' })
export class ShareHoldingFlagService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/share-holding-flags');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/share-holding-flags');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(shareHoldingFlag: IShareHoldingFlag): Observable<EntityResponseType> {
    return this.http.post<IShareHoldingFlag>(this.resourceUrl, shareHoldingFlag, { observe: 'response' });
  }

  update(shareHoldingFlag: IShareHoldingFlag): Observable<EntityResponseType> {
    return this.http.put<IShareHoldingFlag>(
      `${this.resourceUrl}/${getShareHoldingFlagIdentifier(shareHoldingFlag) as number}`,
      shareHoldingFlag,
      { observe: 'response' }
    );
  }

  partialUpdate(shareHoldingFlag: IShareHoldingFlag): Observable<EntityResponseType> {
    return this.http.patch<IShareHoldingFlag>(
      `${this.resourceUrl}/${getShareHoldingFlagIdentifier(shareHoldingFlag) as number}`,
      shareHoldingFlag,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IShareHoldingFlag>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShareHoldingFlag[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShareHoldingFlag[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addShareHoldingFlagToCollectionIfMissing(
    shareHoldingFlagCollection: IShareHoldingFlag[],
    ...shareHoldingFlagsToCheck: (IShareHoldingFlag | null | undefined)[]
  ): IShareHoldingFlag[] {
    const shareHoldingFlags: IShareHoldingFlag[] = shareHoldingFlagsToCheck.filter(isPresent);
    if (shareHoldingFlags.length > 0) {
      const shareHoldingFlagCollectionIdentifiers = shareHoldingFlagCollection.map(
        shareHoldingFlagItem => getShareHoldingFlagIdentifier(shareHoldingFlagItem)!
      );
      const shareHoldingFlagsToAdd = shareHoldingFlags.filter(shareHoldingFlagItem => {
        const shareHoldingFlagIdentifier = getShareHoldingFlagIdentifier(shareHoldingFlagItem);
        if (shareHoldingFlagIdentifier == null || shareHoldingFlagCollectionIdentifiers.includes(shareHoldingFlagIdentifier)) {
          return false;
        }
        shareHoldingFlagCollectionIdentifiers.push(shareHoldingFlagIdentifier);
        return true;
      });
      return [...shareHoldingFlagsToAdd, ...shareHoldingFlagCollection];
    }
    return shareHoldingFlagCollection;
  }
}
