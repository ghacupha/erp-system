import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICustomerComplaintStatusType, getCustomerComplaintStatusTypeIdentifier } from '../customer-complaint-status-type.model';

export type EntityResponseType = HttpResponse<ICustomerComplaintStatusType>;
export type EntityArrayResponseType = HttpResponse<ICustomerComplaintStatusType[]>;

@Injectable({ providedIn: 'root' })
export class CustomerComplaintStatusTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/customer-complaint-status-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/customer-complaint-status-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(customerComplaintStatusType: ICustomerComplaintStatusType): Observable<EntityResponseType> {
    return this.http.post<ICustomerComplaintStatusType>(this.resourceUrl, customerComplaintStatusType, { observe: 'response' });
  }

  update(customerComplaintStatusType: ICustomerComplaintStatusType): Observable<EntityResponseType> {
    return this.http.put<ICustomerComplaintStatusType>(
      `${this.resourceUrl}/${getCustomerComplaintStatusTypeIdentifier(customerComplaintStatusType) as number}`,
      customerComplaintStatusType,
      { observe: 'response' }
    );
  }

  partialUpdate(customerComplaintStatusType: ICustomerComplaintStatusType): Observable<EntityResponseType> {
    return this.http.patch<ICustomerComplaintStatusType>(
      `${this.resourceUrl}/${getCustomerComplaintStatusTypeIdentifier(customerComplaintStatusType) as number}`,
      customerComplaintStatusType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICustomerComplaintStatusType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomerComplaintStatusType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomerComplaintStatusType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCustomerComplaintStatusTypeToCollectionIfMissing(
    customerComplaintStatusTypeCollection: ICustomerComplaintStatusType[],
    ...customerComplaintStatusTypesToCheck: (ICustomerComplaintStatusType | null | undefined)[]
  ): ICustomerComplaintStatusType[] {
    const customerComplaintStatusTypes: ICustomerComplaintStatusType[] = customerComplaintStatusTypesToCheck.filter(isPresent);
    if (customerComplaintStatusTypes.length > 0) {
      const customerComplaintStatusTypeCollectionIdentifiers = customerComplaintStatusTypeCollection.map(
        customerComplaintStatusTypeItem => getCustomerComplaintStatusTypeIdentifier(customerComplaintStatusTypeItem)!
      );
      const customerComplaintStatusTypesToAdd = customerComplaintStatusTypes.filter(customerComplaintStatusTypeItem => {
        const customerComplaintStatusTypeIdentifier = getCustomerComplaintStatusTypeIdentifier(customerComplaintStatusTypeItem);
        if (
          customerComplaintStatusTypeIdentifier == null ||
          customerComplaintStatusTypeCollectionIdentifiers.includes(customerComplaintStatusTypeIdentifier)
        ) {
          return false;
        }
        customerComplaintStatusTypeCollectionIdentifiers.push(customerComplaintStatusTypeIdentifier);
        return true;
      });
      return [...customerComplaintStatusTypesToAdd, ...customerComplaintStatusTypeCollection];
    }
    return customerComplaintStatusTypeCollection;
  }
}
