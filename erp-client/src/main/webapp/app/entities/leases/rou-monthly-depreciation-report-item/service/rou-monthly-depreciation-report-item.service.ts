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
  IRouMonthlyDepreciationReportItem,
  getRouMonthlyDepreciationReportItemIdentifier,
} from '../rou-monthly-depreciation-report-item.model';

export type EntityResponseType = HttpResponse<IRouMonthlyDepreciationReportItem>;
export type EntityArrayResponseType = HttpResponse<IRouMonthlyDepreciationReportItem[]>;

@Injectable({ providedIn: 'root' })
export class RouMonthlyDepreciationReportItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rou-monthly-depreciation-report-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/rou-monthly-depreciation-report-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouMonthlyDepreciationReportItem);
    return this.http
      .post<IRouMonthlyDepreciationReportItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouMonthlyDepreciationReportItem);
    return this.http
      .put<IRouMonthlyDepreciationReportItem>(
        `${this.resourceUrl}/${getRouMonthlyDepreciationReportItemIdentifier(rouMonthlyDepreciationReportItem) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(rouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouMonthlyDepreciationReportItem);
    return this.http
      .patch<IRouMonthlyDepreciationReportItem>(
        `${this.resourceUrl}/${getRouMonthlyDepreciationReportItemIdentifier(rouMonthlyDepreciationReportItem) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRouMonthlyDepreciationReportItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouMonthlyDepreciationReportItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouMonthlyDepreciationReportItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addRouMonthlyDepreciationReportItemToCollectionIfMissing(
    rouMonthlyDepreciationReportItemCollection: IRouMonthlyDepreciationReportItem[],
    ...rouMonthlyDepreciationReportItemsToCheck: (IRouMonthlyDepreciationReportItem | null | undefined)[]
  ): IRouMonthlyDepreciationReportItem[] {
    const rouMonthlyDepreciationReportItems: IRouMonthlyDepreciationReportItem[] =
      rouMonthlyDepreciationReportItemsToCheck.filter(isPresent);
    if (rouMonthlyDepreciationReportItems.length > 0) {
      const rouMonthlyDepreciationReportItemCollectionIdentifiers = rouMonthlyDepreciationReportItemCollection.map(
        rouMonthlyDepreciationReportItemItem => getRouMonthlyDepreciationReportItemIdentifier(rouMonthlyDepreciationReportItemItem)!
      );
      const rouMonthlyDepreciationReportItemsToAdd = rouMonthlyDepreciationReportItems.filter(rouMonthlyDepreciationReportItemItem => {
        const rouMonthlyDepreciationReportItemIdentifier = getRouMonthlyDepreciationReportItemIdentifier(
          rouMonthlyDepreciationReportItemItem
        );
        if (
          rouMonthlyDepreciationReportItemIdentifier == null ||
          rouMonthlyDepreciationReportItemCollectionIdentifiers.includes(rouMonthlyDepreciationReportItemIdentifier)
        ) {
          return false;
        }
        rouMonthlyDepreciationReportItemCollectionIdentifiers.push(rouMonthlyDepreciationReportItemIdentifier);
        return true;
      });
      return [...rouMonthlyDepreciationReportItemsToAdd, ...rouMonthlyDepreciationReportItemCollection];
    }
    return rouMonthlyDepreciationReportItemCollection;
  }

  protected convertDateFromClient(rouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem): IRouMonthlyDepreciationReportItem {
    return Object.assign({}, rouMonthlyDepreciationReportItem, {
      fiscalMonthStartDate: rouMonthlyDepreciationReportItem.fiscalMonthStartDate?.isValid()
        ? rouMonthlyDepreciationReportItem.fiscalMonthStartDate.format(DATE_FORMAT)
        : undefined,
      fiscalMonthEndDate: rouMonthlyDepreciationReportItem.fiscalMonthEndDate?.isValid()
        ? rouMonthlyDepreciationReportItem.fiscalMonthEndDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fiscalMonthStartDate = res.body.fiscalMonthStartDate ? dayjs(res.body.fiscalMonthStartDate) : undefined;
      res.body.fiscalMonthEndDate = res.body.fiscalMonthEndDate ? dayjs(res.body.fiscalMonthEndDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rouMonthlyDepreciationReportItem: IRouMonthlyDepreciationReportItem) => {
        rouMonthlyDepreciationReportItem.fiscalMonthStartDate = rouMonthlyDepreciationReportItem.fiscalMonthStartDate
          ? dayjs(rouMonthlyDepreciationReportItem.fiscalMonthStartDate)
          : undefined;
        rouMonthlyDepreciationReportItem.fiscalMonthEndDate = rouMonthlyDepreciationReportItem.fiscalMonthEndDate
          ? dayjs(rouMonthlyDepreciationReportItem.fiscalMonthEndDate)
          : undefined;
      });
    }
    return res;
  }
}
