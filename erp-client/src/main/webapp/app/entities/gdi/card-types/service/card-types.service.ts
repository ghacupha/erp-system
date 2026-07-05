import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICardTypes, getCardTypesIdentifier } from '../card-types.model';

export type EntityResponseType = HttpResponse<ICardTypes>;
export type EntityArrayResponseType = HttpResponse<ICardTypes[]>;

@Injectable({ providedIn: 'root' })
export class CardTypesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/card-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/card-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cardTypes: ICardTypes): Observable<EntityResponseType> {
    return this.http.post<ICardTypes>(this.resourceUrl, cardTypes, { observe: 'response' });
  }

  update(cardTypes: ICardTypes): Observable<EntityResponseType> {
    return this.http.put<ICardTypes>(`${this.resourceUrl}/${getCardTypesIdentifier(cardTypes) as number}`, cardTypes, {
      observe: 'response',
    });
  }

  partialUpdate(cardTypes: ICardTypes): Observable<EntityResponseType> {
    return this.http.patch<ICardTypes>(`${this.resourceUrl}/${getCardTypesIdentifier(cardTypes) as number}`, cardTypes, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICardTypes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardTypes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardTypes[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCardTypesToCollectionIfMissing(
    cardTypesCollection: ICardTypes[],
    ...cardTypesToCheck: (ICardTypes | null | undefined)[]
  ): ICardTypes[] {
    const cardTypes: ICardTypes[] = cardTypesToCheck.filter(isPresent);
    if (cardTypes.length > 0) {
      const cardTypesCollectionIdentifiers = cardTypesCollection.map(cardTypesItem => getCardTypesIdentifier(cardTypesItem)!);
      const cardTypesToAdd = cardTypes.filter(cardTypesItem => {
        const cardTypesIdentifier = getCardTypesIdentifier(cardTypesItem);
        if (cardTypesIdentifier == null || cardTypesCollectionIdentifiers.includes(cardTypesIdentifier)) {
          return false;
        }
        cardTypesCollectionIdentifiers.push(cardTypesIdentifier);
        return true;
      });
      return [...cardTypesToAdd, ...cardTypesCollection];
    }
    return cardTypesCollection;
  }
}
