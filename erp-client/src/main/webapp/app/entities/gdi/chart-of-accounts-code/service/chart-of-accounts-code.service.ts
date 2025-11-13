///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
import { IChartOfAccountsCode, getChartOfAccountsCodeIdentifier } from '../chart-of-accounts-code.model';

export type EntityResponseType = HttpResponse<IChartOfAccountsCode>;
export type EntityArrayResponseType = HttpResponse<IChartOfAccountsCode[]>;

@Injectable({ providedIn: 'root' })
export class ChartOfAccountsCodeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chart-of-accounts-codes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/chart-of-accounts-codes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(chartOfAccountsCode: IChartOfAccountsCode): Observable<EntityResponseType> {
    return this.http.post<IChartOfAccountsCode>(this.resourceUrl, chartOfAccountsCode, { observe: 'response' });
  }

  update(chartOfAccountsCode: IChartOfAccountsCode): Observable<EntityResponseType> {
    return this.http.put<IChartOfAccountsCode>(
      `${this.resourceUrl}/${getChartOfAccountsCodeIdentifier(chartOfAccountsCode) as number}`,
      chartOfAccountsCode,
      { observe: 'response' }
    );
  }

  partialUpdate(chartOfAccountsCode: IChartOfAccountsCode): Observable<EntityResponseType> {
    return this.http.patch<IChartOfAccountsCode>(
      `${this.resourceUrl}/${getChartOfAccountsCodeIdentifier(chartOfAccountsCode) as number}`,
      chartOfAccountsCode,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChartOfAccountsCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChartOfAccountsCode[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChartOfAccountsCode[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addChartOfAccountsCodeToCollectionIfMissing(
    chartOfAccountsCodeCollection: IChartOfAccountsCode[],
    ...chartOfAccountsCodesToCheck: (IChartOfAccountsCode | null | undefined)[]
  ): IChartOfAccountsCode[] {
    const chartOfAccountsCodes: IChartOfAccountsCode[] = chartOfAccountsCodesToCheck.filter(isPresent);
    if (chartOfAccountsCodes.length > 0) {
      const chartOfAccountsCodeCollectionIdentifiers = chartOfAccountsCodeCollection.map(
        chartOfAccountsCodeItem => getChartOfAccountsCodeIdentifier(chartOfAccountsCodeItem)!
      );
      const chartOfAccountsCodesToAdd = chartOfAccountsCodes.filter(chartOfAccountsCodeItem => {
        const chartOfAccountsCodeIdentifier = getChartOfAccountsCodeIdentifier(chartOfAccountsCodeItem);
        if (chartOfAccountsCodeIdentifier == null || chartOfAccountsCodeCollectionIdentifiers.includes(chartOfAccountsCodeIdentifier)) {
          return false;
        }
        chartOfAccountsCodeCollectionIdentifiers.push(chartOfAccountsCodeIdentifier);
        return true;
      });
      return [...chartOfAccountsCodesToAdd, ...chartOfAccountsCodeCollection];
    }
    return chartOfAccountsCodeCollection;
  }
}
