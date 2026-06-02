import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICardPerformanceFlag, getCardPerformanceFlagIdentifier } from '../card-performance-flag.model';

export type EntityResponseType = HttpResponse<ICardPerformanceFlag>;
export type EntityArrayResponseType = HttpResponse<ICardPerformanceFlag[]>;

@Injectable({ providedIn: 'root' })
export class CardPerformanceFlagService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/card-performance-flags');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/card-performance-flags');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cardPerformanceFlag: ICardPerformanceFlag): Observable<EntityResponseType> {
    return this.http.post<ICardPerformanceFlag>(this.resourceUrl, cardPerformanceFlag, { observe: 'response' });
  }

  update(cardPerformanceFlag: ICardPerformanceFlag): Observable<EntityResponseType> {
    return this.http.put<ICardPerformanceFlag>(
      `${this.resourceUrl}/${getCardPerformanceFlagIdentifier(cardPerformanceFlag) as number}`,
      cardPerformanceFlag,
      { observe: 'response' }
    );
  }

  partialUpdate(cardPerformanceFlag: ICardPerformanceFlag): Observable<EntityResponseType> {
    return this.http.patch<ICardPerformanceFlag>(
      `${this.resourceUrl}/${getCardPerformanceFlagIdentifier(cardPerformanceFlag) as number}`,
      cardPerformanceFlag,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICardPerformanceFlag>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardPerformanceFlag[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardPerformanceFlag[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCardPerformanceFlagToCollectionIfMissing(
    cardPerformanceFlagCollection: ICardPerformanceFlag[],
    ...cardPerformanceFlagsToCheck: (ICardPerformanceFlag | null | undefined)[]
  ): ICardPerformanceFlag[] {
    const cardPerformanceFlags: ICardPerformanceFlag[] = cardPerformanceFlagsToCheck.filter(isPresent);
    if (cardPerformanceFlags.length > 0) {
      const cardPerformanceFlagCollectionIdentifiers = cardPerformanceFlagCollection.map(
        cardPerformanceFlagItem => getCardPerformanceFlagIdentifier(cardPerformanceFlagItem)!
      );
      const cardPerformanceFlagsToAdd = cardPerformanceFlags.filter(cardPerformanceFlagItem => {
        const cardPerformanceFlagIdentifier = getCardPerformanceFlagIdentifier(cardPerformanceFlagItem);
        if (cardPerformanceFlagIdentifier == null || cardPerformanceFlagCollectionIdentifiers.includes(cardPerformanceFlagIdentifier)) {
          return false;
        }
        cardPerformanceFlagCollectionIdentifiers.push(cardPerformanceFlagIdentifier);
        return true;
      });
      return [...cardPerformanceFlagsToAdd, ...cardPerformanceFlagCollection];
    }
    return cardPerformanceFlagCollection;
  }
}
