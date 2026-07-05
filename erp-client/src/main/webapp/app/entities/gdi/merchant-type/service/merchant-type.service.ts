import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMerchantType, getMerchantTypeIdentifier } from '../merchant-type.model';

export type EntityResponseType = HttpResponse<IMerchantType>;
export type EntityArrayResponseType = HttpResponse<IMerchantType[]>;

@Injectable({ providedIn: 'root' })
export class MerchantTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/merchant-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/merchant-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(merchantType: IMerchantType): Observable<EntityResponseType> {
    return this.http.post<IMerchantType>(this.resourceUrl, merchantType, { observe: 'response' });
  }

  update(merchantType: IMerchantType): Observable<EntityResponseType> {
    return this.http.put<IMerchantType>(`${this.resourceUrl}/${getMerchantTypeIdentifier(merchantType) as number}`, merchantType, {
      observe: 'response',
    });
  }

  partialUpdate(merchantType: IMerchantType): Observable<EntityResponseType> {
    return this.http.patch<IMerchantType>(`${this.resourceUrl}/${getMerchantTypeIdentifier(merchantType) as number}`, merchantType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMerchantType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMerchantType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMerchantType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addMerchantTypeToCollectionIfMissing(
    merchantTypeCollection: IMerchantType[],
    ...merchantTypesToCheck: (IMerchantType | null | undefined)[]
  ): IMerchantType[] {
    const merchantTypes: IMerchantType[] = merchantTypesToCheck.filter(isPresent);
    if (merchantTypes.length > 0) {
      const merchantTypeCollectionIdentifiers = merchantTypeCollection.map(
        merchantTypeItem => getMerchantTypeIdentifier(merchantTypeItem)!
      );
      const merchantTypesToAdd = merchantTypes.filter(merchantTypeItem => {
        const merchantTypeIdentifier = getMerchantTypeIdentifier(merchantTypeItem);
        if (merchantTypeIdentifier == null || merchantTypeCollectionIdentifiers.includes(merchantTypeIdentifier)) {
          return false;
        }
        merchantTypeCollectionIdentifiers.push(merchantTypeIdentifier);
        return true;
      });
      return [...merchantTypesToAdd, ...merchantTypeCollection];
    }
    return merchantTypeCollection;
  }
}
