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
import { ILeaseLiabilityScheduleItem, getLeaseLiabilityScheduleItemIdentifier } from '../lease-liability-schedule-item.model';

export type EntityResponseType = HttpResponse<ILeaseLiabilityScheduleItem>;
export type EntityArrayResponseType = HttpResponse<ILeaseLiabilityScheduleItem[]>;

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityScheduleItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/lease-liability-schedule-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/lease-liability-schedule-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem): Observable<EntityResponseType> {
    return this.http.post<ILeaseLiabilityScheduleItem>(this.resourceUrl, leaseLiabilityScheduleItem, { observe: 'response' });
  }

  update(leaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem): Observable<EntityResponseType> {
    return this.http.put<ILeaseLiabilityScheduleItem>(
      `${this.resourceUrl}/${getLeaseLiabilityScheduleItemIdentifier(leaseLiabilityScheduleItem) as number}`,
      leaseLiabilityScheduleItem,
      { observe: 'response' }
    );
  }

  partialUpdate(leaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem): Observable<EntityResponseType> {
    return this.http.patch<ILeaseLiabilityScheduleItem>(
      `${this.resourceUrl}/${getLeaseLiabilityScheduleItemIdentifier(leaseLiabilityScheduleItem) as number}`,
      leaseLiabilityScheduleItem,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeaseLiabilityScheduleItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseLiabilityScheduleItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseLiabilityScheduleItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLeaseLiabilityScheduleItemToCollectionIfMissing(
    leaseLiabilityScheduleItemCollection: ILeaseLiabilityScheduleItem[],
    ...leaseLiabilityScheduleItemsToCheck: (ILeaseLiabilityScheduleItem | null | undefined)[]
  ): ILeaseLiabilityScheduleItem[] {
    const leaseLiabilityScheduleItems: ILeaseLiabilityScheduleItem[] = leaseLiabilityScheduleItemsToCheck.filter(isPresent);
    if (leaseLiabilityScheduleItems.length > 0) {
      const leaseLiabilityScheduleItemCollectionIdentifiers = leaseLiabilityScheduleItemCollection.map(
        leaseLiabilityScheduleItemItem => getLeaseLiabilityScheduleItemIdentifier(leaseLiabilityScheduleItemItem)!
      );
      const leaseLiabilityScheduleItemsToAdd = leaseLiabilityScheduleItems.filter(leaseLiabilityScheduleItemItem => {
        const leaseLiabilityScheduleItemIdentifier = getLeaseLiabilityScheduleItemIdentifier(leaseLiabilityScheduleItemItem);
        if (
          leaseLiabilityScheduleItemIdentifier == null ||
          leaseLiabilityScheduleItemCollectionIdentifiers.includes(leaseLiabilityScheduleItemIdentifier)
        ) {
          return false;
        }
        leaseLiabilityScheduleItemCollectionIdentifiers.push(leaseLiabilityScheduleItemIdentifier);
        return true;
      });
      return [...leaseLiabilityScheduleItemsToAdd, ...leaseLiabilityScheduleItemCollection];
    }
    return leaseLiabilityScheduleItemCollection;
  }
}
