import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILoanApplicationType, getLoanApplicationTypeIdentifier } from '../loan-application-type.model';

export type EntityResponseType = HttpResponse<ILoanApplicationType>;
export type EntityArrayResponseType = HttpResponse<ILoanApplicationType[]>;

@Injectable({ providedIn: 'root' })
export class LoanApplicationTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/loan-application-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/loan-application-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(loanApplicationType: ILoanApplicationType): Observable<EntityResponseType> {
    return this.http.post<ILoanApplicationType>(this.resourceUrl, loanApplicationType, { observe: 'response' });
  }

  update(loanApplicationType: ILoanApplicationType): Observable<EntityResponseType> {
    return this.http.put<ILoanApplicationType>(
      `${this.resourceUrl}/${getLoanApplicationTypeIdentifier(loanApplicationType) as number}`,
      loanApplicationType,
      { observe: 'response' }
    );
  }

  partialUpdate(loanApplicationType: ILoanApplicationType): Observable<EntityResponseType> {
    return this.http.patch<ILoanApplicationType>(
      `${this.resourceUrl}/${getLoanApplicationTypeIdentifier(loanApplicationType) as number}`,
      loanApplicationType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILoanApplicationType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoanApplicationType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoanApplicationType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLoanApplicationTypeToCollectionIfMissing(
    loanApplicationTypeCollection: ILoanApplicationType[],
    ...loanApplicationTypesToCheck: (ILoanApplicationType | null | undefined)[]
  ): ILoanApplicationType[] {
    const loanApplicationTypes: ILoanApplicationType[] = loanApplicationTypesToCheck.filter(isPresent);
    if (loanApplicationTypes.length > 0) {
      const loanApplicationTypeCollectionIdentifiers = loanApplicationTypeCollection.map(
        loanApplicationTypeItem => getLoanApplicationTypeIdentifier(loanApplicationTypeItem)!
      );
      const loanApplicationTypesToAdd = loanApplicationTypes.filter(loanApplicationTypeItem => {
        const loanApplicationTypeIdentifier = getLoanApplicationTypeIdentifier(loanApplicationTypeItem);
        if (loanApplicationTypeIdentifier == null || loanApplicationTypeCollectionIdentifiers.includes(loanApplicationTypeIdentifier)) {
          return false;
        }
        loanApplicationTypeCollectionIdentifiers.push(loanApplicationTypeIdentifier);
        return true;
      });
      return [...loanApplicationTypesToAdd, ...loanApplicationTypeCollection];
    }
    return loanApplicationTypeCollection;
  }
}
