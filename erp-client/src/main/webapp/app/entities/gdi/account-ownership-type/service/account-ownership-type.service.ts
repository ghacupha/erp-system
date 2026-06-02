import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAccountOwnershipType, getAccountOwnershipTypeIdentifier } from '../account-ownership-type.model';

export type EntityResponseType = HttpResponse<IAccountOwnershipType>;
export type EntityArrayResponseType = HttpResponse<IAccountOwnershipType[]>;

@Injectable({ providedIn: 'root' })
export class AccountOwnershipTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/account-ownership-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/account-ownership-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAccountOwnershipType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccountOwnershipType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccountOwnershipType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addAccountOwnershipTypeToCollectionIfMissing(
    accountOwnershipTypeCollection: IAccountOwnershipType[],
    ...accountOwnershipTypesToCheck: (IAccountOwnershipType | null | undefined)[]
  ): IAccountOwnershipType[] {
    const accountOwnershipTypes: IAccountOwnershipType[] = accountOwnershipTypesToCheck.filter(isPresent);
    if (accountOwnershipTypes.length > 0) {
      const accountOwnershipTypeCollectionIdentifiers = accountOwnershipTypeCollection.map(
        accountOwnershipTypeItem => getAccountOwnershipTypeIdentifier(accountOwnershipTypeItem)!
      );
      const accountOwnershipTypesToAdd = accountOwnershipTypes.filter(accountOwnershipTypeItem => {
        const accountOwnershipTypeIdentifier = getAccountOwnershipTypeIdentifier(accountOwnershipTypeItem);
        if (accountOwnershipTypeIdentifier == null || accountOwnershipTypeCollectionIdentifiers.includes(accountOwnershipTypeIdentifier)) {
          return false;
        }
        accountOwnershipTypeCollectionIdentifiers.push(accountOwnershipTypeIdentifier);
        return true;
      });
      return [...accountOwnershipTypesToAdd, ...accountOwnershipTypeCollection];
    }
    return accountOwnershipTypeCollection;
  }
}
