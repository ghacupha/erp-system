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
import {
  ILeaseLiabilityScheduleReportItem,
  getLeaseLiabilityScheduleReportItemIdentifier,
} from '../lease-liability-schedule-report-item.model';

export type EntityResponseType = HttpResponse<ILeaseLiabilityScheduleReportItem>;
export type EntityArrayResponseType = HttpResponse<ILeaseLiabilityScheduleReportItem[]>;

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityScheduleReportItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/lease-liability-schedule-report-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/lease-liability-schedule-report-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeaseLiabilityScheduleReportItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseLiabilityScheduleReportItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseLiabilityScheduleReportItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLeaseLiabilityScheduleReportItemToCollectionIfMissing(
    leaseLiabilityScheduleReportItemCollection: ILeaseLiabilityScheduleReportItem[],
    ...leaseLiabilityScheduleReportItemsToCheck: (ILeaseLiabilityScheduleReportItem | null | undefined)[]
  ): ILeaseLiabilityScheduleReportItem[] {
    const leaseLiabilityScheduleReportItems: ILeaseLiabilityScheduleReportItem[] =
      leaseLiabilityScheduleReportItemsToCheck.filter(isPresent);
    if (leaseLiabilityScheduleReportItems.length > 0) {
      const leaseLiabilityScheduleReportItemCollectionIdentifiers = leaseLiabilityScheduleReportItemCollection.map(
        leaseLiabilityScheduleReportItemItem => getLeaseLiabilityScheduleReportItemIdentifier(leaseLiabilityScheduleReportItemItem)!
      );
      const leaseLiabilityScheduleReportItemsToAdd = leaseLiabilityScheduleReportItems.filter(leaseLiabilityScheduleReportItemItem => {
        const leaseLiabilityScheduleReportItemIdentifier = getLeaseLiabilityScheduleReportItemIdentifier(
          leaseLiabilityScheduleReportItemItem
        );
        if (
          leaseLiabilityScheduleReportItemIdentifier == null ||
          leaseLiabilityScheduleReportItemCollectionIdentifiers.includes(leaseLiabilityScheduleReportItemIdentifier)
        ) {
          return false;
        }
        leaseLiabilityScheduleReportItemCollectionIdentifiers.push(leaseLiabilityScheduleReportItemIdentifier);
        return true;
      });
      return [...leaseLiabilityScheduleReportItemsToAdd, ...leaseLiabilityScheduleReportItemCollection];
    }
    return leaseLiabilityScheduleReportItemCollection;
  }
}
