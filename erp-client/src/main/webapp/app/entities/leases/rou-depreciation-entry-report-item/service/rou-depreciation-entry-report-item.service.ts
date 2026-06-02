import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRouDepreciationEntryReportItem, getRouDepreciationEntryReportItemIdentifier } from '../rou-depreciation-entry-report-item.model';

export type EntityResponseType = HttpResponse<IRouDepreciationEntryReportItem>;
export type EntityArrayResponseType = HttpResponse<IRouDepreciationEntryReportItem[]>;

@Injectable({ providedIn: 'root' })
export class RouDepreciationEntryReportItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rou-depreciation-entry-report-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/rou-depreciation-entry-report-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRouDepreciationEntryReportItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouDepreciationEntryReportItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouDepreciationEntryReportItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addRouDepreciationEntryReportItemToCollectionIfMissing(
    rouDepreciationEntryReportItemCollection: IRouDepreciationEntryReportItem[],
    ...rouDepreciationEntryReportItemsToCheck: (IRouDepreciationEntryReportItem | null | undefined)[]
  ): IRouDepreciationEntryReportItem[] {
    const rouDepreciationEntryReportItems: IRouDepreciationEntryReportItem[] = rouDepreciationEntryReportItemsToCheck.filter(isPresent);
    if (rouDepreciationEntryReportItems.length > 0) {
      const rouDepreciationEntryReportItemCollectionIdentifiers = rouDepreciationEntryReportItemCollection.map(
        rouDepreciationEntryReportItemItem => getRouDepreciationEntryReportItemIdentifier(rouDepreciationEntryReportItemItem)!
      );
      const rouDepreciationEntryReportItemsToAdd = rouDepreciationEntryReportItems.filter(rouDepreciationEntryReportItemItem => {
        const rouDepreciationEntryReportItemIdentifier = getRouDepreciationEntryReportItemIdentifier(rouDepreciationEntryReportItemItem);
        if (
          rouDepreciationEntryReportItemIdentifier == null ||
          rouDepreciationEntryReportItemCollectionIdentifiers.includes(rouDepreciationEntryReportItemIdentifier)
        ) {
          return false;
        }
        rouDepreciationEntryReportItemCollectionIdentifiers.push(rouDepreciationEntryReportItemIdentifier);
        return true;
      });
      return [...rouDepreciationEntryReportItemsToAdd, ...rouDepreciationEntryReportItemCollection];
    }
    return rouDepreciationEntryReportItemCollection;
  }

  protected convertDateFromClient(rouDepreciationEntryReportItem: IRouDepreciationEntryReportItem): IRouDepreciationEntryReportItem {
    return Object.assign({}, rouDepreciationEntryReportItem, {
      fiscalPeriodEndDate: rouDepreciationEntryReportItem.fiscalPeriodEndDate?.isValid()
        ? rouDepreciationEntryReportItem.fiscalPeriodEndDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fiscalPeriodEndDate = res.body.fiscalPeriodEndDate ? dayjs(res.body.fiscalPeriodEndDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rouDepreciationEntryReportItem: IRouDepreciationEntryReportItem) => {
        rouDepreciationEntryReportItem.fiscalPeriodEndDate = rouDepreciationEntryReportItem.fiscalPeriodEndDate
          ? dayjs(rouDepreciationEntryReportItem.fiscalPeriodEndDate)
          : undefined;
      });
    }
    return res;
  }
}
