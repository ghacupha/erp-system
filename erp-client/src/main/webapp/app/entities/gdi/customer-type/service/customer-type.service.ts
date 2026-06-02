import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICustomerType, getCustomerTypeIdentifier } from '../customer-type.model';

export type EntityResponseType = HttpResponse<ICustomerType>;
export type EntityArrayResponseType = HttpResponse<ICustomerType[]>;

@Injectable({ providedIn: 'root' })
export class CustomerTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/customer-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/customer-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(customerType: ICustomerType): Observable<EntityResponseType> {
    return this.http.post<ICustomerType>(this.resourceUrl, customerType, { observe: 'response' });
  }

  update(customerType: ICustomerType): Observable<EntityResponseType> {
    return this.http.put<ICustomerType>(`${this.resourceUrl}/${getCustomerTypeIdentifier(customerType) as number}`, customerType, {
      observe: 'response',
    });
  }

  partialUpdate(customerType: ICustomerType): Observable<EntityResponseType> {
    return this.http.patch<ICustomerType>(`${this.resourceUrl}/${getCustomerTypeIdentifier(customerType) as number}`, customerType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICustomerType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomerType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomerType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCustomerTypeToCollectionIfMissing(
    customerTypeCollection: ICustomerType[],
    ...customerTypesToCheck: (ICustomerType | null | undefined)[]
  ): ICustomerType[] {
    const customerTypes: ICustomerType[] = customerTypesToCheck.filter(isPresent);
    if (customerTypes.length > 0) {
      const customerTypeCollectionIdentifiers = customerTypeCollection.map(
        customerTypeItem => getCustomerTypeIdentifier(customerTypeItem)!
      );
      const customerTypesToAdd = customerTypes.filter(customerTypeItem => {
        const customerTypeIdentifier = getCustomerTypeIdentifier(customerTypeItem);
        if (customerTypeIdentifier == null || customerTypeCollectionIdentifiers.includes(customerTypeIdentifier)) {
          return false;
        }
        customerTypeCollectionIdentifiers.push(customerTypeIdentifier);
        return true;
      });
      return [...customerTypesToAdd, ...customerTypeCollection];
    }
    return customerTypeCollection;
  }
}
