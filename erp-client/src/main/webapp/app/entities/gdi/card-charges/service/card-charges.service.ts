import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICardCharges, getCardChargesIdentifier } from '../card-charges.model';

export type EntityResponseType = HttpResponse<ICardCharges>;
export type EntityArrayResponseType = HttpResponse<ICardCharges[]>;

@Injectable({ providedIn: 'root' })
export class CardChargesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/card-charges');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/card-charges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cardCharges: ICardCharges): Observable<EntityResponseType> {
    return this.http.post<ICardCharges>(this.resourceUrl, cardCharges, { observe: 'response' });
  }

  update(cardCharges: ICardCharges): Observable<EntityResponseType> {
    return this.http.put<ICardCharges>(`${this.resourceUrl}/${getCardChargesIdentifier(cardCharges) as number}`, cardCharges, {
      observe: 'response',
    });
  }

  partialUpdate(cardCharges: ICardCharges): Observable<EntityResponseType> {
    return this.http.patch<ICardCharges>(`${this.resourceUrl}/${getCardChargesIdentifier(cardCharges) as number}`, cardCharges, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICardCharges>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardCharges[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICardCharges[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCardChargesToCollectionIfMissing(
    cardChargesCollection: ICardCharges[],
    ...cardChargesToCheck: (ICardCharges | null | undefined)[]
  ): ICardCharges[] {
    const cardCharges: ICardCharges[] = cardChargesToCheck.filter(isPresent);
    if (cardCharges.length > 0) {
      const cardChargesCollectionIdentifiers = cardChargesCollection.map(cardChargesItem => getCardChargesIdentifier(cardChargesItem)!);
      const cardChargesToAdd = cardCharges.filter(cardChargesItem => {
        const cardChargesIdentifier = getCardChargesIdentifier(cardChargesItem);
        if (cardChargesIdentifier == null || cardChargesCollectionIdentifiers.includes(cardChargesIdentifier)) {
          return false;
        }
        cardChargesCollectionIdentifiers.push(cardChargesIdentifier);
        return true;
      });
      return [...cardChargesToAdd, ...cardChargesCollection];
    }
    return cardChargesCollection;
  }
}
