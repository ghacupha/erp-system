import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILoanRestructureItem, getLoanRestructureItemIdentifier } from '../loan-restructure-item.model';

export type EntityResponseType = HttpResponse<ILoanRestructureItem>;
export type EntityArrayResponseType = HttpResponse<ILoanRestructureItem[]>;

@Injectable({ providedIn: 'root' })
export class LoanRestructureItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/loan-restructure-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/loan-restructure-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(loanRestructureItem: ILoanRestructureItem): Observable<EntityResponseType> {
    return this.http.post<ILoanRestructureItem>(this.resourceUrl, loanRestructureItem, { observe: 'response' });
  }

  update(loanRestructureItem: ILoanRestructureItem): Observable<EntityResponseType> {
    return this.http.put<ILoanRestructureItem>(
      `${this.resourceUrl}/${getLoanRestructureItemIdentifier(loanRestructureItem) as number}`,
      loanRestructureItem,
      { observe: 'response' }
    );
  }

  partialUpdate(loanRestructureItem: ILoanRestructureItem): Observable<EntityResponseType> {
    return this.http.patch<ILoanRestructureItem>(
      `${this.resourceUrl}/${getLoanRestructureItemIdentifier(loanRestructureItem) as number}`,
      loanRestructureItem,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILoanRestructureItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoanRestructureItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoanRestructureItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLoanRestructureItemToCollectionIfMissing(
    loanRestructureItemCollection: ILoanRestructureItem[],
    ...loanRestructureItemsToCheck: (ILoanRestructureItem | null | undefined)[]
  ): ILoanRestructureItem[] {
    const loanRestructureItems: ILoanRestructureItem[] = loanRestructureItemsToCheck.filter(isPresent);
    if (loanRestructureItems.length > 0) {
      const loanRestructureItemCollectionIdentifiers = loanRestructureItemCollection.map(
        loanRestructureItemItem => getLoanRestructureItemIdentifier(loanRestructureItemItem)!
      );
      const loanRestructureItemsToAdd = loanRestructureItems.filter(loanRestructureItemItem => {
        const loanRestructureItemIdentifier = getLoanRestructureItemIdentifier(loanRestructureItemItem);
        if (loanRestructureItemIdentifier == null || loanRestructureItemCollectionIdentifiers.includes(loanRestructureItemIdentifier)) {
          return false;
        }
        loanRestructureItemCollectionIdentifiers.push(loanRestructureItemIdentifier);
        return true;
      });
      return [...loanRestructureItemsToAdd, ...loanRestructureItemCollection];
    }
    return loanRestructureItemCollection;
  }
}
