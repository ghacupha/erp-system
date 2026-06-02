import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITALeaseRepaymentRule, getTALeaseRepaymentRuleIdentifier } from '../ta-lease-repayment-rule.model';

export type EntityResponseType = HttpResponse<ITALeaseRepaymentRule>;
export type EntityArrayResponseType = HttpResponse<ITALeaseRepaymentRule[]>;

@Injectable({ providedIn: 'root' })
export class TALeaseRepaymentRuleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ta-lease-repayment-rules');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/ta-lease-repayment-rules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tALeaseRepaymentRule: ITALeaseRepaymentRule): Observable<EntityResponseType> {
    return this.http.post<ITALeaseRepaymentRule>(this.resourceUrl, tALeaseRepaymentRule, { observe: 'response' });
  }

  update(tALeaseRepaymentRule: ITALeaseRepaymentRule): Observable<EntityResponseType> {
    return this.http.put<ITALeaseRepaymentRule>(
      `${this.resourceUrl}/${getTALeaseRepaymentRuleIdentifier(tALeaseRepaymentRule) as number}`,
      tALeaseRepaymentRule,
      { observe: 'response' }
    );
  }

  partialUpdate(tALeaseRepaymentRule: ITALeaseRepaymentRule): Observable<EntityResponseType> {
    return this.http.patch<ITALeaseRepaymentRule>(
      `${this.resourceUrl}/${getTALeaseRepaymentRuleIdentifier(tALeaseRepaymentRule) as number}`,
      tALeaseRepaymentRule,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITALeaseRepaymentRule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITALeaseRepaymentRule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITALeaseRepaymentRule[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addTALeaseRepaymentRuleToCollectionIfMissing(
    tALeaseRepaymentRuleCollection: ITALeaseRepaymentRule[],
    ...tALeaseRepaymentRulesToCheck: (ITALeaseRepaymentRule | null | undefined)[]
  ): ITALeaseRepaymentRule[] {
    const tALeaseRepaymentRules: ITALeaseRepaymentRule[] = tALeaseRepaymentRulesToCheck.filter(isPresent);
    if (tALeaseRepaymentRules.length > 0) {
      const tALeaseRepaymentRuleCollectionIdentifiers = tALeaseRepaymentRuleCollection.map(
        tALeaseRepaymentRuleItem => getTALeaseRepaymentRuleIdentifier(tALeaseRepaymentRuleItem)!
      );
      const tALeaseRepaymentRulesToAdd = tALeaseRepaymentRules.filter(tALeaseRepaymentRuleItem => {
        const tALeaseRepaymentRuleIdentifier = getTALeaseRepaymentRuleIdentifier(tALeaseRepaymentRuleItem);
        if (tALeaseRepaymentRuleIdentifier == null || tALeaseRepaymentRuleCollectionIdentifiers.includes(tALeaseRepaymentRuleIdentifier)) {
          return false;
        }
        tALeaseRepaymentRuleCollectionIdentifiers.push(tALeaseRepaymentRuleIdentifier);
        return true;
      });
      return [...tALeaseRepaymentRulesToAdd, ...tALeaseRepaymentRuleCollection];
    }
    return tALeaseRepaymentRuleCollection;
  }
}
