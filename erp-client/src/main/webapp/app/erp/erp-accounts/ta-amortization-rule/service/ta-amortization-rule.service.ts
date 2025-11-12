///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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
import { ITAAmortizationRule, getTAAmortizationRuleIdentifier } from '../ta-amortization-rule.model';

export type EntityResponseType = HttpResponse<ITAAmortizationRule>;
export type EntityArrayResponseType = HttpResponse<ITAAmortizationRule[]>;

@Injectable({ providedIn: 'root' })
export class TAAmortizationRuleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/accounts/ta-amortization-rules');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/accounts/_search/ta-amortization-rules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tAAmortizationRule: ITAAmortizationRule): Observable<EntityResponseType> {
    return this.http.post<ITAAmortizationRule>(this.resourceUrl, tAAmortizationRule, { observe: 'response' });
  }

  update(tAAmortizationRule: ITAAmortizationRule): Observable<EntityResponseType> {
    return this.http.put<ITAAmortizationRule>(
      `${this.resourceUrl}/${getTAAmortizationRuleIdentifier(tAAmortizationRule) as number}`,
      tAAmortizationRule,
      { observe: 'response' }
    );
  }

  partialUpdate(tAAmortizationRule: ITAAmortizationRule): Observable<EntityResponseType> {
    return this.http.patch<ITAAmortizationRule>(
      `${this.resourceUrl}/${getTAAmortizationRuleIdentifier(tAAmortizationRule) as number}`,
      tAAmortizationRule,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITAAmortizationRule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITAAmortizationRule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITAAmortizationRule[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addTAAmortizationRuleToCollectionIfMissing(
    tAAmortizationRuleCollection: ITAAmortizationRule[],
    ...tAAmortizationRulesToCheck: (ITAAmortizationRule | null | undefined)[]
  ): ITAAmortizationRule[] {
    const tAAmortizationRules: ITAAmortizationRule[] = tAAmortizationRulesToCheck.filter(isPresent);
    if (tAAmortizationRules.length > 0) {
      const tAAmortizationRuleCollectionIdentifiers = tAAmortizationRuleCollection.map(
        tAAmortizationRuleItem => getTAAmortizationRuleIdentifier(tAAmortizationRuleItem)!
      );
      const tAAmortizationRulesToAdd = tAAmortizationRules.filter(tAAmortizationRuleItem => {
        const tAAmortizationRuleIdentifier = getTAAmortizationRuleIdentifier(tAAmortizationRuleItem);
        if (tAAmortizationRuleIdentifier == null || tAAmortizationRuleCollectionIdentifiers.includes(tAAmortizationRuleIdentifier)) {
          return false;
        }
        tAAmortizationRuleCollectionIdentifiers.push(tAAmortizationRuleIdentifier);
        return true;
      });
      return [...tAAmortizationRulesToAdd, ...tAAmortizationRuleCollection];
    }
    return tAAmortizationRuleCollection;
  }
}
