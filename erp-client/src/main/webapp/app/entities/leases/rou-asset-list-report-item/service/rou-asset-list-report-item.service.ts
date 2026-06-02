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
import { IRouAssetListReportItem, getRouAssetListReportItemIdentifier } from '../rou-asset-list-report-item.model';

export type EntityResponseType = HttpResponse<IRouAssetListReportItem>;
export type EntityArrayResponseType = HttpResponse<IRouAssetListReportItem[]>;

@Injectable({ providedIn: 'root' })
export class RouAssetListReportItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rou-asset-list-report-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/rou-asset-list-report-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRouAssetListReportItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouAssetListReportItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouAssetListReportItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addRouAssetListReportItemToCollectionIfMissing(
    rouAssetListReportItemCollection: IRouAssetListReportItem[],
    ...rouAssetListReportItemsToCheck: (IRouAssetListReportItem | null | undefined)[]
  ): IRouAssetListReportItem[] {
    const rouAssetListReportItems: IRouAssetListReportItem[] = rouAssetListReportItemsToCheck.filter(isPresent);
    if (rouAssetListReportItems.length > 0) {
      const rouAssetListReportItemCollectionIdentifiers = rouAssetListReportItemCollection.map(
        rouAssetListReportItemItem => getRouAssetListReportItemIdentifier(rouAssetListReportItemItem)!
      );
      const rouAssetListReportItemsToAdd = rouAssetListReportItems.filter(rouAssetListReportItemItem => {
        const rouAssetListReportItemIdentifier = getRouAssetListReportItemIdentifier(rouAssetListReportItemItem);
        if (
          rouAssetListReportItemIdentifier == null ||
          rouAssetListReportItemCollectionIdentifiers.includes(rouAssetListReportItemIdentifier)
        ) {
          return false;
        }
        rouAssetListReportItemCollectionIdentifiers.push(rouAssetListReportItemIdentifier);
        return true;
      });
      return [...rouAssetListReportItemsToAdd, ...rouAssetListReportItemCollection];
    }
    return rouAssetListReportItemCollection;
  }

  protected convertDateFromClient(rouAssetListReportItem: IRouAssetListReportItem): IRouAssetListReportItem {
    return Object.assign({}, rouAssetListReportItem, {
      commencementDate: rouAssetListReportItem.commencementDate?.isValid()
        ? rouAssetListReportItem.commencementDate.format(DATE_FORMAT)
        : undefined,
      expirationDate: rouAssetListReportItem.expirationDate?.isValid()
        ? rouAssetListReportItem.expirationDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.commencementDate = res.body.commencementDate ? dayjs(res.body.commencementDate) : undefined;
      res.body.expirationDate = res.body.expirationDate ? dayjs(res.body.expirationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rouAssetListReportItem: IRouAssetListReportItem) => {
        rouAssetListReportItem.commencementDate = rouAssetListReportItem.commencementDate
          ? dayjs(rouAssetListReportItem.commencementDate)
          : undefined;
        rouAssetListReportItem.expirationDate = rouAssetListReportItem.expirationDate
          ? dayjs(rouAssetListReportItem.expirationDate)
          : undefined;
      });
    }
    return res;
  }
}
