import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILoanDeclineReason, getLoanDeclineReasonIdentifier } from '../loan-decline-reason.model';

export type EntityResponseType = HttpResponse<ILoanDeclineReason>;
export type EntityArrayResponseType = HttpResponse<ILoanDeclineReason[]>;

@Injectable({ providedIn: 'root' })
export class LoanDeclineReasonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/loan-decline-reasons');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/loan-decline-reasons');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(loanDeclineReason: ILoanDeclineReason): Observable<EntityResponseType> {
    return this.http.post<ILoanDeclineReason>(this.resourceUrl, loanDeclineReason, { observe: 'response' });
  }

  update(loanDeclineReason: ILoanDeclineReason): Observable<EntityResponseType> {
    return this.http.put<ILoanDeclineReason>(
      `${this.resourceUrl}/${getLoanDeclineReasonIdentifier(loanDeclineReason) as number}`,
      loanDeclineReason,
      { observe: 'response' }
    );
  }

  partialUpdate(loanDeclineReason: ILoanDeclineReason): Observable<EntityResponseType> {
    return this.http.patch<ILoanDeclineReason>(
      `${this.resourceUrl}/${getLoanDeclineReasonIdentifier(loanDeclineReason) as number}`,
      loanDeclineReason,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILoanDeclineReason>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoanDeclineReason[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoanDeclineReason[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLoanDeclineReasonToCollectionIfMissing(
    loanDeclineReasonCollection: ILoanDeclineReason[],
    ...loanDeclineReasonsToCheck: (ILoanDeclineReason | null | undefined)[]
  ): ILoanDeclineReason[] {
    const loanDeclineReasons: ILoanDeclineReason[] = loanDeclineReasonsToCheck.filter(isPresent);
    if (loanDeclineReasons.length > 0) {
      const loanDeclineReasonCollectionIdentifiers = loanDeclineReasonCollection.map(
        loanDeclineReasonItem => getLoanDeclineReasonIdentifier(loanDeclineReasonItem)!
      );
      const loanDeclineReasonsToAdd = loanDeclineReasons.filter(loanDeclineReasonItem => {
        const loanDeclineReasonIdentifier = getLoanDeclineReasonIdentifier(loanDeclineReasonItem);
        if (loanDeclineReasonIdentifier == null || loanDeclineReasonCollectionIdentifiers.includes(loanDeclineReasonIdentifier)) {
          return false;
        }
        loanDeclineReasonCollectionIdentifiers.push(loanDeclineReasonIdentifier);
        return true;
      });
      return [...loanDeclineReasonsToAdd, ...loanDeclineReasonCollection];
    }
    return loanDeclineReasonCollection;
  }
}
