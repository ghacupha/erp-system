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
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lease-liability-schedule-report-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/lease-liability-schedule-report-items');

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
