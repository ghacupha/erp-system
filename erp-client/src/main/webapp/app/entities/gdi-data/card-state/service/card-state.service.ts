import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICardState, getCardStateIdentifier } from '../card-state.model';

export type EntityResponseType = HttpResponse<ICardState>;
export type EntityArrayResponseType = HttpResponse<ICardState[]>;

@Injectable({ providedIn: 'root' })
export class CardStateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/card-states');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/card-states');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cardState: ICardState): Observable<EntityResponseType> {
    return this.http.post<ICardState>(this.resourceUrl, cardState, { observe: 'response' });
  }

  update(cardState: ICardState): Observable<EntityResponseType> {
    return this.http.put<ICardState>(`${this.resourceUrl}/${getCardStateIdentifier(cardState) as number}`, cardState, {
      observe: 'response',
    });
  }

  partialUpdate(cardState: ICardState): Observable<EntityResponseType> {
    return this.http.patch<ICardState>(`${this.resourceUrl}/${getCardStateIdentifier(cardState) as number}`, cardState, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICardState>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardState[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardState[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCardStateToCollectionIfMissing(
    cardStateCollection: ICardState[],
    ...cardStatesToCheck: (ICardState | null | undefined)[]
  ): ICardState[] {
    const cardStates: ICardState[] = cardStatesToCheck.filter(isPresent);
    if (cardStates.length > 0) {
      const cardStateCollectionIdentifiers = cardStateCollection.map(cardStateItem => getCardStateIdentifier(cardStateItem)!);
      const cardStatesToAdd = cardStates.filter(cardStateItem => {
        const cardStateIdentifier = getCardStateIdentifier(cardStateItem);
        if (cardStateIdentifier == null || cardStateCollectionIdentifiers.includes(cardStateIdentifier)) {
          return false;
        }
        cardStateCollectionIdentifiers.push(cardStateIdentifier);
        return true;
      });
      return [...cardStatesToAdd, ...cardStateCollection];
    }
    return cardStateCollection;
  }
}
