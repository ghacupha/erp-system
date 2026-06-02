import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILoanAccountCategory, getLoanAccountCategoryIdentifier } from '../loan-account-category.model';

export type EntityResponseType = HttpResponse<ILoanAccountCategory>;
export type EntityArrayResponseType = HttpResponse<ILoanAccountCategory[]>;

@Injectable({ providedIn: 'root' })
export class LoanAccountCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/loan-account-categories');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/loan-account-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(loanAccountCategory: ILoanAccountCategory): Observable<EntityResponseType> {
    return this.http.post<ILoanAccountCategory>(this.resourceUrl, loanAccountCategory, { observe: 'response' });
  }

  update(loanAccountCategory: ILoanAccountCategory): Observable<EntityResponseType> {
    return this.http.put<ILoanAccountCategory>(
      `${this.resourceUrl}/${getLoanAccountCategoryIdentifier(loanAccountCategory) as number}`,
      loanAccountCategory,
      { observe: 'response' }
    );
  }

  partialUpdate(loanAccountCategory: ILoanAccountCategory): Observable<EntityResponseType> {
    return this.http.patch<ILoanAccountCategory>(
      `${this.resourceUrl}/${getLoanAccountCategoryIdentifier(loanAccountCategory) as number}`,
      loanAccountCategory,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILoanAccountCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoanAccountCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoanAccountCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLoanAccountCategoryToCollectionIfMissing(
    loanAccountCategoryCollection: ILoanAccountCategory[],
    ...loanAccountCategoriesToCheck: (ILoanAccountCategory | null | undefined)[]
  ): ILoanAccountCategory[] {
    const loanAccountCategories: ILoanAccountCategory[] = loanAccountCategoriesToCheck.filter(isPresent);
    if (loanAccountCategories.length > 0) {
      const loanAccountCategoryCollectionIdentifiers = loanAccountCategoryCollection.map(
        loanAccountCategoryItem => getLoanAccountCategoryIdentifier(loanAccountCategoryItem)!
      );
      const loanAccountCategoriesToAdd = loanAccountCategories.filter(loanAccountCategoryItem => {
        const loanAccountCategoryIdentifier = getLoanAccountCategoryIdentifier(loanAccountCategoryItem);
        if (loanAccountCategoryIdentifier == null || loanAccountCategoryCollectionIdentifiers.includes(loanAccountCategoryIdentifier)) {
          return false;
        }
        loanAccountCategoryCollectionIdentifiers.push(loanAccountCategoryIdentifier);
        return true;
      });
      return [...loanAccountCategoriesToAdd, ...loanAccountCategoryCollection];
    }
    return loanAccountCategoryCollection;
  }
}
