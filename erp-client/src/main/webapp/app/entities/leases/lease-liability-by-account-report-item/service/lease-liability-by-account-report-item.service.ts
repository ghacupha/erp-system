import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import {
  ILeaseLiabilityByAccountReportItem,
  getLeaseLiabilityByAccountReportItemIdentifier,
} from '../lease-liability-by-account-report-item.model';

export type EntityResponseType = HttpResponse<ILeaseLiabilityByAccountReportItem>;
export type EntityArrayResponseType = HttpResponse<ILeaseLiabilityByAccountReportItem[]>;

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityByAccountReportItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lease-liability-by-account-report-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/lease-liability-by-account-report-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeaseLiabilityByAccountReportItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseLiabilityByAccountReportItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseLiabilityByAccountReportItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLeaseLiabilityByAccountReportItemToCollectionIfMissing(
    leaseLiabilityByAccountReportItemCollection: ILeaseLiabilityByAccountReportItem[],
    ...leaseLiabilityByAccountReportItemsToCheck: (ILeaseLiabilityByAccountReportItem | null | undefined)[]
  ): ILeaseLiabilityByAccountReportItem[] {
    const leaseLiabilityByAccountReportItems: ILeaseLiabilityByAccountReportItem[] =
      leaseLiabilityByAccountReportItemsToCheck.filter(isPresent);
    if (leaseLiabilityByAccountReportItems.length > 0) {
      const leaseLiabilityByAccountReportItemCollectionIdentifiers = leaseLiabilityByAccountReportItemCollection.map(
        leaseLiabilityByAccountReportItemItem => getLeaseLiabilityByAccountReportItemIdentifier(leaseLiabilityByAccountReportItemItem)!
      );
      const leaseLiabilityByAccountReportItemsToAdd = leaseLiabilityByAccountReportItems.filter(leaseLiabilityByAccountReportItemItem => {
        const leaseLiabilityByAccountReportItemIdentifier = getLeaseLiabilityByAccountReportItemIdentifier(
          leaseLiabilityByAccountReportItemItem
        );
        if (
          leaseLiabilityByAccountReportItemIdentifier == null ||
          leaseLiabilityByAccountReportItemCollectionIdentifiers.includes(leaseLiabilityByAccountReportItemIdentifier)
        ) {
          return false;
        }
        leaseLiabilityByAccountReportItemCollectionIdentifiers.push(leaseLiabilityByAccountReportItemIdentifier);
        return true;
      });
      return [...leaseLiabilityByAccountReportItemsToAdd, ...leaseLiabilityByAccountReportItemCollection];
    }
    return leaseLiabilityByAccountReportItemCollection;
  }
}
