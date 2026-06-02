import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICardCategoryType, getCardCategoryTypeIdentifier } from '../card-category-type.model';

export type EntityResponseType = HttpResponse<ICardCategoryType>;
export type EntityArrayResponseType = HttpResponse<ICardCategoryType[]>;

@Injectable({ providedIn: 'root' })
export class CardCategoryTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/card-category-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/card-category-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cardCategoryType: ICardCategoryType): Observable<EntityResponseType> {
    return this.http.post<ICardCategoryType>(this.resourceUrl, cardCategoryType, { observe: 'response' });
  }

  update(cardCategoryType: ICardCategoryType): Observable<EntityResponseType> {
    return this.http.put<ICardCategoryType>(
      `${this.resourceUrl}/${getCardCategoryTypeIdentifier(cardCategoryType) as number}`,
      cardCategoryType,
      { observe: 'response' }
    );
  }

  partialUpdate(cardCategoryType: ICardCategoryType): Observable<EntityResponseType> {
    return this.http.patch<ICardCategoryType>(
      `${this.resourceUrl}/${getCardCategoryTypeIdentifier(cardCategoryType) as number}`,
      cardCategoryType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICardCategoryType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardCategoryType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardCategoryType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCardCategoryTypeToCollectionIfMissing(
    cardCategoryTypeCollection: ICardCategoryType[],
    ...cardCategoryTypesToCheck: (ICardCategoryType | null | undefined)[]
  ): ICardCategoryType[] {
    const cardCategoryTypes: ICardCategoryType[] = cardCategoryTypesToCheck.filter(isPresent);
    if (cardCategoryTypes.length > 0) {
      const cardCategoryTypeCollectionIdentifiers = cardCategoryTypeCollection.map(
        cardCategoryTypeItem => getCardCategoryTypeIdentifier(cardCategoryTypeItem)!
      );
      const cardCategoryTypesToAdd = cardCategoryTypes.filter(cardCategoryTypeItem => {
        const cardCategoryTypeIdentifier = getCardCategoryTypeIdentifier(cardCategoryTypeItem);
        if (cardCategoryTypeIdentifier == null || cardCategoryTypeCollectionIdentifiers.includes(cardCategoryTypeIdentifier)) {
          return false;
        }
        cardCategoryTypeCollectionIdentifiers.push(cardCategoryTypeIdentifier);
        return true;
      });
      return [...cardCategoryTypesToAdd, ...cardCategoryTypeCollection];
    }
    return cardCategoryTypeCollection;
  }
}
