import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IKenyanCurrencyDenomination, getKenyanCurrencyDenominationIdentifier } from '../kenyan-currency-denomination.model';

export type EntityResponseType = HttpResponse<IKenyanCurrencyDenomination>;
export type EntityArrayResponseType = HttpResponse<IKenyanCurrencyDenomination[]>;

@Injectable({ providedIn: 'root' })
export class KenyanCurrencyDenominationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/kenyan-currency-denominations');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/kenyan-currency-denominations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(kenyanCurrencyDenomination: IKenyanCurrencyDenomination): Observable<EntityResponseType> {
    return this.http.post<IKenyanCurrencyDenomination>(this.resourceUrl, kenyanCurrencyDenomination, { observe: 'response' });
  }

  update(kenyanCurrencyDenomination: IKenyanCurrencyDenomination): Observable<EntityResponseType> {
    return this.http.put<IKenyanCurrencyDenomination>(
      `${this.resourceUrl}/${getKenyanCurrencyDenominationIdentifier(kenyanCurrencyDenomination) as number}`,
      kenyanCurrencyDenomination,
      { observe: 'response' }
    );
  }

  partialUpdate(kenyanCurrencyDenomination: IKenyanCurrencyDenomination): Observable<EntityResponseType> {
    return this.http.patch<IKenyanCurrencyDenomination>(
      `${this.resourceUrl}/${getKenyanCurrencyDenominationIdentifier(kenyanCurrencyDenomination) as number}`,
      kenyanCurrencyDenomination,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IKenyanCurrencyDenomination>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IKenyanCurrencyDenomination[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IKenyanCurrencyDenomination[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addKenyanCurrencyDenominationToCollectionIfMissing(
    kenyanCurrencyDenominationCollection: IKenyanCurrencyDenomination[],
    ...kenyanCurrencyDenominationsToCheck: (IKenyanCurrencyDenomination | null | undefined)[]
  ): IKenyanCurrencyDenomination[] {
    const kenyanCurrencyDenominations: IKenyanCurrencyDenomination[] = kenyanCurrencyDenominationsToCheck.filter(isPresent);
    if (kenyanCurrencyDenominations.length > 0) {
      const kenyanCurrencyDenominationCollectionIdentifiers = kenyanCurrencyDenominationCollection.map(
        kenyanCurrencyDenominationItem => getKenyanCurrencyDenominationIdentifier(kenyanCurrencyDenominationItem)!
      );
      const kenyanCurrencyDenominationsToAdd = kenyanCurrencyDenominations.filter(kenyanCurrencyDenominationItem => {
        const kenyanCurrencyDenominationIdentifier = getKenyanCurrencyDenominationIdentifier(kenyanCurrencyDenominationItem);
        if (
          kenyanCurrencyDenominationIdentifier == null ||
          kenyanCurrencyDenominationCollectionIdentifiers.includes(kenyanCurrencyDenominationIdentifier)
        ) {
          return false;
        }
        kenyanCurrencyDenominationCollectionIdentifiers.push(kenyanCurrencyDenominationIdentifier);
        return true;
      });
      return [...kenyanCurrencyDenominationsToAdd, ...kenyanCurrencyDenominationCollection];
    }
    return kenyanCurrencyDenominationCollection;
  }
}
