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
import { IWIPListItem, getWIPListItemIdentifier } from '../wip-list-item.model';

export type EntityResponseType = HttpResponse<IWIPListItem>;
export type EntityArrayResponseType = HttpResponse<IWIPListItem[]>;

@Injectable({ providedIn: 'root' })
export class WIPListItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/wip-list-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/wip-list-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWIPListItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWIPListItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWIPListItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addWIPListItemToCollectionIfMissing(
    wIPListItemCollection: IWIPListItem[],
    ...wIPListItemsToCheck: (IWIPListItem | null | undefined)[]
  ): IWIPListItem[] {
    const wIPListItems: IWIPListItem[] = wIPListItemsToCheck.filter(isPresent);
    if (wIPListItems.length > 0) {
      const wIPListItemCollectionIdentifiers = wIPListItemCollection.map(wIPListItemItem => getWIPListItemIdentifier(wIPListItemItem)!);
      const wIPListItemsToAdd = wIPListItems.filter(wIPListItemItem => {
        const wIPListItemIdentifier = getWIPListItemIdentifier(wIPListItemItem);
        if (wIPListItemIdentifier == null || wIPListItemCollectionIdentifiers.includes(wIPListItemIdentifier)) {
          return false;
        }
        wIPListItemCollectionIdentifiers.push(wIPListItemIdentifier);
        return true;
      });
      return [...wIPListItemsToAdd, ...wIPListItemCollection];
    }
    return wIPListItemCollection;
  }

  protected convertDateFromClient(wIPListItem: IWIPListItem): IWIPListItem {
    return Object.assign({}, wIPListItem, {
      instalmentDate: wIPListItem.instalmentDate?.isValid() ? wIPListItem.instalmentDate.format(DATE_FORMAT) : undefined,
      settlementTransactionDate: wIPListItem.settlementTransactionDate?.isValid()
        ? wIPListItem.settlementTransactionDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.instalmentDate = res.body.instalmentDate ? dayjs(res.body.instalmentDate) : undefined;
      res.body.settlementTransactionDate = res.body.settlementTransactionDate ? dayjs(res.body.settlementTransactionDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((wIPListItem: IWIPListItem) => {
        wIPListItem.instalmentDate = wIPListItem.instalmentDate ? dayjs(wIPListItem.instalmentDate) : undefined;
        wIPListItem.settlementTransactionDate = wIPListItem.settlementTransactionDate
          ? dayjs(wIPListItem.settlementTransactionDate)
          : undefined;
      });
    }
    return res;
  }
}
