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
import { ITaxRule, getTaxRuleIdentifier } from '../tax-rule.model';

export type EntityResponseType = HttpResponse<ITaxRule>;
export type EntityArrayResponseType = HttpResponse<ITaxRule[]>;

@Injectable({ providedIn: 'root' })
export class TaxRuleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tax-rules');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/tax-rules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(taxRule: ITaxRule): Observable<EntityResponseType> {
    return this.http.post<ITaxRule>(this.resourceUrl, taxRule, { observe: 'response' });
  }

  update(taxRule: ITaxRule): Observable<EntityResponseType> {
    return this.http.put<ITaxRule>(`${this.resourceUrl}/${getTaxRuleIdentifier(taxRule) as number}`, taxRule, { observe: 'response' });
  }

  partialUpdate(taxRule: ITaxRule): Observable<EntityResponseType> {
    return this.http.patch<ITaxRule>(`${this.resourceUrl}/${getTaxRuleIdentifier(taxRule) as number}`, taxRule, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITaxRule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITaxRule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITaxRule[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addTaxRuleToCollectionIfMissing(taxRuleCollection: ITaxRule[], ...taxRulesToCheck: (ITaxRule | null | undefined)[]): ITaxRule[] {
    const taxRules: ITaxRule[] = taxRulesToCheck.filter(isPresent);
    if (taxRules.length > 0) {
      const taxRuleCollectionIdentifiers = taxRuleCollection.map(taxRuleItem => getTaxRuleIdentifier(taxRuleItem)!);
      const taxRulesToAdd = taxRules.filter(taxRuleItem => {
        const taxRuleIdentifier = getTaxRuleIdentifier(taxRuleItem);
        if (taxRuleIdentifier == null || taxRuleCollectionIdentifiers.includes(taxRuleIdentifier)) {
          return false;
        }
        taxRuleCollectionIdentifiers.push(taxRuleIdentifier);
        return true;
      });
      return [...taxRulesToAdd, ...taxRuleCollection];
    }
    return taxRuleCollection;
  }
}
