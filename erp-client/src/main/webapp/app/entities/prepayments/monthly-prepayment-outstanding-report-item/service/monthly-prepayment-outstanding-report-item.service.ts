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
import {
  IMonthlyPrepaymentOutstandingReportItem,
  getMonthlyPrepaymentOutstandingReportItemIdentifier,
} from '../monthly-prepayment-outstanding-report-item.model';

export type EntityResponseType = HttpResponse<IMonthlyPrepaymentOutstandingReportItem>;
export type EntityArrayResponseType = HttpResponse<IMonthlyPrepaymentOutstandingReportItem[]>;

@Injectable({ providedIn: 'root' })
export class MonthlyPrepaymentOutstandingReportItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/monthly-prepayment-outstanding-report-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/monthly-prepayment-outstanding-report-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMonthlyPrepaymentOutstandingReportItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMonthlyPrepaymentOutstandingReportItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMonthlyPrepaymentOutstandingReportItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addMonthlyPrepaymentOutstandingReportItemToCollectionIfMissing(
    monthlyPrepaymentOutstandingReportItemCollection: IMonthlyPrepaymentOutstandingReportItem[],
    ...monthlyPrepaymentOutstandingReportItemsToCheck: (IMonthlyPrepaymentOutstandingReportItem | null | undefined)[]
  ): IMonthlyPrepaymentOutstandingReportItem[] {
    const monthlyPrepaymentOutstandingReportItems: IMonthlyPrepaymentOutstandingReportItem[] =
      monthlyPrepaymentOutstandingReportItemsToCheck.filter(isPresent);
    if (monthlyPrepaymentOutstandingReportItems.length > 0) {
      const monthlyPrepaymentOutstandingReportItemCollectionIdentifiers = monthlyPrepaymentOutstandingReportItemCollection.map(
        monthlyPrepaymentOutstandingReportItemItem =>
          getMonthlyPrepaymentOutstandingReportItemIdentifier(monthlyPrepaymentOutstandingReportItemItem)!
      );
      const monthlyPrepaymentOutstandingReportItemsToAdd = monthlyPrepaymentOutstandingReportItems.filter(
        monthlyPrepaymentOutstandingReportItemItem => {
          const monthlyPrepaymentOutstandingReportItemIdentifier = getMonthlyPrepaymentOutstandingReportItemIdentifier(
            monthlyPrepaymentOutstandingReportItemItem
          );
          if (
            monthlyPrepaymentOutstandingReportItemIdentifier == null ||
            monthlyPrepaymentOutstandingReportItemCollectionIdentifiers.includes(monthlyPrepaymentOutstandingReportItemIdentifier)
          ) {
            return false;
          }
          monthlyPrepaymentOutstandingReportItemCollectionIdentifiers.push(monthlyPrepaymentOutstandingReportItemIdentifier);
          return true;
        }
      );
      return [...monthlyPrepaymentOutstandingReportItemsToAdd, ...monthlyPrepaymentOutstandingReportItemCollection];
    }
    return monthlyPrepaymentOutstandingReportItemCollection;
  }

  protected convertDateFromClient(
    monthlyPrepaymentOutstandingReportItem: IMonthlyPrepaymentOutstandingReportItem
  ): IMonthlyPrepaymentOutstandingReportItem {
    return Object.assign({}, monthlyPrepaymentOutstandingReportItem, {
      fiscalMonthEndDate: monthlyPrepaymentOutstandingReportItem.fiscalMonthEndDate?.isValid()
        ? monthlyPrepaymentOutstandingReportItem.fiscalMonthEndDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fiscalMonthEndDate = res.body.fiscalMonthEndDate ? dayjs(res.body.fiscalMonthEndDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((monthlyPrepaymentOutstandingReportItem: IMonthlyPrepaymentOutstandingReportItem) => {
        monthlyPrepaymentOutstandingReportItem.fiscalMonthEndDate = monthlyPrepaymentOutstandingReportItem.fiscalMonthEndDate
          ? dayjs(monthlyPrepaymentOutstandingReportItem.fiscalMonthEndDate)
          : undefined;
      });
    }
    return res;
  }
}
