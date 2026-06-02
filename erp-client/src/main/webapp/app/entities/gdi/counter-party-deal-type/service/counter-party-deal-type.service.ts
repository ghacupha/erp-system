import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICounterPartyDealType, getCounterPartyDealTypeIdentifier } from '../counter-party-deal-type.model';

export type EntityResponseType = HttpResponse<ICounterPartyDealType>;
export type EntityArrayResponseType = HttpResponse<ICounterPartyDealType[]>;

@Injectable({ providedIn: 'root' })
export class CounterPartyDealTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/counter-party-deal-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/counter-party-deal-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(counterPartyDealType: ICounterPartyDealType): Observable<EntityResponseType> {
    return this.http.post<ICounterPartyDealType>(this.resourceUrl, counterPartyDealType, { observe: 'response' });
  }

  update(counterPartyDealType: ICounterPartyDealType): Observable<EntityResponseType> {
    return this.http.put<ICounterPartyDealType>(
      `${this.resourceUrl}/${getCounterPartyDealTypeIdentifier(counterPartyDealType) as number}`,
      counterPartyDealType,
      { observe: 'response' }
    );
  }

  partialUpdate(counterPartyDealType: ICounterPartyDealType): Observable<EntityResponseType> {
    return this.http.patch<ICounterPartyDealType>(
      `${this.resourceUrl}/${getCounterPartyDealTypeIdentifier(counterPartyDealType) as number}`,
      counterPartyDealType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICounterPartyDealType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICounterPartyDealType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICounterPartyDealType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCounterPartyDealTypeToCollectionIfMissing(
    counterPartyDealTypeCollection: ICounterPartyDealType[],
    ...counterPartyDealTypesToCheck: (ICounterPartyDealType | null | undefined)[]
  ): ICounterPartyDealType[] {
    const counterPartyDealTypes: ICounterPartyDealType[] = counterPartyDealTypesToCheck.filter(isPresent);
    if (counterPartyDealTypes.length > 0) {
      const counterPartyDealTypeCollectionIdentifiers = counterPartyDealTypeCollection.map(
        counterPartyDealTypeItem => getCounterPartyDealTypeIdentifier(counterPartyDealTypeItem)!
      );
      const counterPartyDealTypesToAdd = counterPartyDealTypes.filter(counterPartyDealTypeItem => {
        const counterPartyDealTypeIdentifier = getCounterPartyDealTypeIdentifier(counterPartyDealTypeItem);
        if (counterPartyDealTypeIdentifier == null || counterPartyDealTypeCollectionIdentifiers.includes(counterPartyDealTypeIdentifier)) {
          return false;
        }
        counterPartyDealTypeCollectionIdentifiers.push(counterPartyDealTypeIdentifier);
        return true;
      });
      return [...counterPartyDealTypesToAdd, ...counterPartyDealTypeCollection];
    }
    return counterPartyDealTypeCollection;
  }
}
