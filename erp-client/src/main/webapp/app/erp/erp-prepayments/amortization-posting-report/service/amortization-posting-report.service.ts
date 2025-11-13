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
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAmortizationPostingReport, getAmortizationPostingReportIdentifier } from '../amortization-posting-report.model';

export type EntityResponseType = HttpResponse<IAmortizationPostingReport>;
export type EntityArrayResponseType = HttpResponse<IAmortizationPostingReport[]>;

@Injectable({ providedIn: 'root' })
export class AmortizationPostingReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prepayments/amortization-posting-reports/reported');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/prepayments/_search/amortization-posting-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  findByDate(reportDate: string): Observable<EntityResponseType> {
    let reportParams: HttpParams = new HttpParams();
    reportParams = reportParams.set('reportDate', reportDate);

    return this.http.get<IAmortizationPostingReport>(`${this.resourceUrl}`, { params: reportParams, observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAmortizationPostingReport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAmortizationPostingReport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryByDate(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAmortizationPostingReport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAmortizationPostingReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addAmortizationPostingReportToCollectionIfMissing(
    amortizationPostingReportCollection: IAmortizationPostingReport[],
    ...amortizationPostingReportsToCheck: (IAmortizationPostingReport | null | undefined)[]
  ): IAmortizationPostingReport[] {
    const amortizationPostingReports: IAmortizationPostingReport[] = amortizationPostingReportsToCheck.filter(isPresent);
    if (amortizationPostingReports.length > 0) {
      const amortizationPostingReportCollectionIdentifiers = amortizationPostingReportCollection.map(
        amortizationPostingReportItem => getAmortizationPostingReportIdentifier(amortizationPostingReportItem)!
      );
      const amortizationPostingReportsToAdd = amortizationPostingReports.filter(amortizationPostingReportItem => {
        const amortizationPostingReportIdentifier = getAmortizationPostingReportIdentifier(amortizationPostingReportItem);
        if (
          amortizationPostingReportIdentifier == null ||
          amortizationPostingReportCollectionIdentifiers.includes(amortizationPostingReportIdentifier)
        ) {
          return false;
        }
        amortizationPostingReportCollectionIdentifiers.push(amortizationPostingReportIdentifier);
        return true;
      });
      return [...amortizationPostingReportsToAdd, ...amortizationPostingReportCollection];
    }
    return amortizationPostingReportCollection;
  }
}
