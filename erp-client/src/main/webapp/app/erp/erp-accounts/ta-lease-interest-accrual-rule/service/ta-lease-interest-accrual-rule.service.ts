///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITALeaseInterestAccrualRule, getTALeaseInterestAccrualRuleIdentifier } from '../ta-lease-interest-accrual-rule.model';

export type EntityResponseType = HttpResponse<ITALeaseInterestAccrualRule>;
export type EntityArrayResponseType = HttpResponse<ITALeaseInterestAccrualRule[]>;

@Injectable({ providedIn: 'root' })
export class TALeaseInterestAccrualRuleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/accounts/ta-lease-interest-accrual-rules');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/accounts/_search/ta-lease-interest-accrual-rules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tALeaseInterestAccrualRule: ITALeaseInterestAccrualRule): Observable<EntityResponseType> {
    return this.http.post<ITALeaseInterestAccrualRule>(this.resourceUrl, tALeaseInterestAccrualRule, { observe: 'response' });
  }

  update(tALeaseInterestAccrualRule: ITALeaseInterestAccrualRule): Observable<EntityResponseType> {
    return this.http.put<ITALeaseInterestAccrualRule>(
      `${this.resourceUrl}/${getTALeaseInterestAccrualRuleIdentifier(tALeaseInterestAccrualRule) as number}`,
      tALeaseInterestAccrualRule,
      { observe: 'response' }
    );
  }

  partialUpdate(tALeaseInterestAccrualRule: ITALeaseInterestAccrualRule): Observable<EntityResponseType> {
    return this.http.patch<ITALeaseInterestAccrualRule>(
      `${this.resourceUrl}/${getTALeaseInterestAccrualRuleIdentifier(tALeaseInterestAccrualRule) as number}`,
      tALeaseInterestAccrualRule,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITALeaseInterestAccrualRule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITALeaseInterestAccrualRule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITALeaseInterestAccrualRule[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addTALeaseInterestAccrualRuleToCollectionIfMissing(
    tALeaseInterestAccrualRuleCollection: ITALeaseInterestAccrualRule[],
    ...tALeaseInterestAccrualRulesToCheck: (ITALeaseInterestAccrualRule | null | undefined)[]
  ): ITALeaseInterestAccrualRule[] {
    const tALeaseInterestAccrualRules: ITALeaseInterestAccrualRule[] = tALeaseInterestAccrualRulesToCheck.filter(isPresent);
    if (tALeaseInterestAccrualRules.length > 0) {
      const tALeaseInterestAccrualRuleCollectionIdentifiers = tALeaseInterestAccrualRuleCollection.map(
        tALeaseInterestAccrualRuleItem => getTALeaseInterestAccrualRuleIdentifier(tALeaseInterestAccrualRuleItem)!
      );
      const tALeaseInterestAccrualRulesToAdd = tALeaseInterestAccrualRules.filter(tALeaseInterestAccrualRuleItem => {
        const tALeaseInterestAccrualRuleIdentifier = getTALeaseInterestAccrualRuleIdentifier(tALeaseInterestAccrualRuleItem);
        if (
          tALeaseInterestAccrualRuleIdentifier == null ||
          tALeaseInterestAccrualRuleCollectionIdentifiers.includes(tALeaseInterestAccrualRuleIdentifier)
        ) {
          return false;
        }
        tALeaseInterestAccrualRuleCollectionIdentifiers.push(tALeaseInterestAccrualRuleIdentifier);
        return true;
      });
      return [...tALeaseInterestAccrualRulesToAdd, ...tALeaseInterestAccrualRuleCollection];
    }
    return tALeaseInterestAccrualRuleCollection;
  }
}
