import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILeaseLiabilityReportItem, getLeaseLiabilityReportItemIdentifier } from '../lease-liability-report-item.model';

export type EntityResponseType = HttpResponse<ILeaseLiabilityReportItem>;
export type EntityArrayResponseType = HttpResponse<ILeaseLiabilityReportItem[]>;

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityReportItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lease-liability-report-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/lease-liability-report-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeaseLiabilityReportItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseLiabilityReportItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseLiabilityReportItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLeaseLiabilityReportItemToCollectionIfMissing(
    leaseLiabilityReportItemCollection: ILeaseLiabilityReportItem[],
    ...leaseLiabilityReportItemsToCheck: (ILeaseLiabilityReportItem | null | undefined)[]
  ): ILeaseLiabilityReportItem[] {
    const leaseLiabilityReportItems: ILeaseLiabilityReportItem[] = leaseLiabilityReportItemsToCheck.filter(isPresent);
    if (leaseLiabilityReportItems.length > 0) {
      const leaseLiabilityReportItemCollectionIdentifiers = leaseLiabilityReportItemCollection.map(
        leaseLiabilityReportItemItem => getLeaseLiabilityReportItemIdentifier(leaseLiabilityReportItemItem)!
      );
      const leaseLiabilityReportItemsToAdd = leaseLiabilityReportItems.filter(leaseLiabilityReportItemItem => {
        const leaseLiabilityReportItemIdentifier = getLeaseLiabilityReportItemIdentifier(leaseLiabilityReportItemItem);
        if (
          leaseLiabilityReportItemIdentifier == null ||
          leaseLiabilityReportItemCollectionIdentifiers.includes(leaseLiabilityReportItemIdentifier)
        ) {
          return false;
        }
        leaseLiabilityReportItemCollectionIdentifiers.push(leaseLiabilityReportItemIdentifier);
        return true;
      });
      return [...leaseLiabilityReportItemsToAdd, ...leaseLiabilityReportItemCollection];
    }
    return leaseLiabilityReportItemCollection;
  }
}
