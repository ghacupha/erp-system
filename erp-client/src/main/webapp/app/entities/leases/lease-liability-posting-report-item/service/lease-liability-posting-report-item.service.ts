import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import {
  ILeaseLiabilityPostingReportItem,
  getLeaseLiabilityPostingReportItemIdentifier,
} from '../lease-liability-posting-report-item.model';

export type EntityResponseType = HttpResponse<ILeaseLiabilityPostingReportItem>;
export type EntityArrayResponseType = HttpResponse<ILeaseLiabilityPostingReportItem[]>;

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityPostingReportItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lease-liability-posting-report-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/lease-liability-posting-report-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeaseLiabilityPostingReportItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseLiabilityPostingReportItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseLiabilityPostingReportItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLeaseLiabilityPostingReportItemToCollectionIfMissing(
    leaseLiabilityPostingReportItemCollection: ILeaseLiabilityPostingReportItem[],
    ...leaseLiabilityPostingReportItemsToCheck: (ILeaseLiabilityPostingReportItem | null | undefined)[]
  ): ILeaseLiabilityPostingReportItem[] {
    const leaseLiabilityPostingReportItems: ILeaseLiabilityPostingReportItem[] = leaseLiabilityPostingReportItemsToCheck.filter(isPresent);
    if (leaseLiabilityPostingReportItems.length > 0) {
      const leaseLiabilityPostingReportItemCollectionIdentifiers = leaseLiabilityPostingReportItemCollection.map(
        leaseLiabilityPostingReportItemItem => getLeaseLiabilityPostingReportItemIdentifier(leaseLiabilityPostingReportItemItem)!
      );
      const leaseLiabilityPostingReportItemsToAdd = leaseLiabilityPostingReportItems.filter(leaseLiabilityPostingReportItemItem => {
        const leaseLiabilityPostingReportItemIdentifier = getLeaseLiabilityPostingReportItemIdentifier(leaseLiabilityPostingReportItemItem);
        if (
          leaseLiabilityPostingReportItemIdentifier == null ||
          leaseLiabilityPostingReportItemCollectionIdentifiers.includes(leaseLiabilityPostingReportItemIdentifier)
        ) {
          return false;
        }
        leaseLiabilityPostingReportItemCollectionIdentifiers.push(leaseLiabilityPostingReportItemIdentifier);
        return true;
      });
      return [...leaseLiabilityPostingReportItemsToAdd, ...leaseLiabilityPostingReportItemCollection];
    }
    return leaseLiabilityPostingReportItemCollection;
  }
}
