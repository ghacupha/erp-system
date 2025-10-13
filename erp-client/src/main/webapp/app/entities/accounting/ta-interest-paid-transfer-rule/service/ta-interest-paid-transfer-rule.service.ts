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
import { ITAInterestPaidTransferRule, getTAInterestPaidTransferRuleIdentifier } from '../ta-interest-paid-transfer-rule.model';

export type EntityResponseType = HttpResponse<ITAInterestPaidTransferRule>;
export type EntityArrayResponseType = HttpResponse<ITAInterestPaidTransferRule[]>;

@Injectable({ providedIn: 'root' })
export class TAInterestPaidTransferRuleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ta-interest-paid-transfer-rules');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/ta-interest-paid-transfer-rules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tAInterestPaidTransferRule: ITAInterestPaidTransferRule): Observable<EntityResponseType> {
    return this.http.post<ITAInterestPaidTransferRule>(this.resourceUrl, tAInterestPaidTransferRule, { observe: 'response' });
  }

  update(tAInterestPaidTransferRule: ITAInterestPaidTransferRule): Observable<EntityResponseType> {
    return this.http.put<ITAInterestPaidTransferRule>(
      `${this.resourceUrl}/${getTAInterestPaidTransferRuleIdentifier(tAInterestPaidTransferRule) as number}`,
      tAInterestPaidTransferRule,
      { observe: 'response' }
    );
  }

  partialUpdate(tAInterestPaidTransferRule: ITAInterestPaidTransferRule): Observable<EntityResponseType> {
    return this.http.patch<ITAInterestPaidTransferRule>(
      `${this.resourceUrl}/${getTAInterestPaidTransferRuleIdentifier(tAInterestPaidTransferRule) as number}`,
      tAInterestPaidTransferRule,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITAInterestPaidTransferRule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITAInterestPaidTransferRule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITAInterestPaidTransferRule[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addTAInterestPaidTransferRuleToCollectionIfMissing(
    tAInterestPaidTransferRuleCollection: ITAInterestPaidTransferRule[],
    ...tAInterestPaidTransferRulesToCheck: (ITAInterestPaidTransferRule | null | undefined)[]
  ): ITAInterestPaidTransferRule[] {
    const tAInterestPaidTransferRules: ITAInterestPaidTransferRule[] = tAInterestPaidTransferRulesToCheck.filter(isPresent);
    if (tAInterestPaidTransferRules.length > 0) {
      const tAInterestPaidTransferRuleCollectionIdentifiers = tAInterestPaidTransferRuleCollection.map(
        tAInterestPaidTransferRuleItem => getTAInterestPaidTransferRuleIdentifier(tAInterestPaidTransferRuleItem)!
      );
      const tAInterestPaidTransferRulesToAdd = tAInterestPaidTransferRules.filter(tAInterestPaidTransferRuleItem => {
        const tAInterestPaidTransferRuleIdentifier = getTAInterestPaidTransferRuleIdentifier(tAInterestPaidTransferRuleItem);
        if (
          tAInterestPaidTransferRuleIdentifier == null ||
          tAInterestPaidTransferRuleCollectionIdentifiers.includes(tAInterestPaidTransferRuleIdentifier)
        ) {
          return false;
        }
        tAInterestPaidTransferRuleCollectionIdentifiers.push(tAInterestPaidTransferRuleIdentifier);
        return true;
      });
      return [...tAInterestPaidTransferRulesToAdd, ...tAInterestPaidTransferRuleCollection];
    }
    return tAInterestPaidTransferRuleCollection;
  }
}
