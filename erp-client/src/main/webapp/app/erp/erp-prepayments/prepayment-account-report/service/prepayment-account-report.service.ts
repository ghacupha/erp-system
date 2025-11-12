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
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IPrepaymentAccountReport, getPrepaymentAccountReportIdentifier } from '../prepayment-account-report.model';

export type EntityResponseType = HttpResponse<IPrepaymentAccountReport>;
export type EntityArrayResponseType = HttpResponse<IPrepaymentAccountReport[]>;

@Injectable({ providedIn: 'root' })
export class PrepaymentAccountReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prepayments/prepayment-account-reports');
  protected reportsUrl = this.applicationConfigService.getEndpointFor('api/prepayments/prepayment-account-reports/reported');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/prepayments/_search/prepayment-account-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrepaymentAccountReport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByReportDate(reportDate: string, id: number): Observable<EntityResponseType> {

    let reportParams: HttpParams = new HttpParams();
    reportParams = reportParams.set('reportDate', reportDate);

    return this.http.get<IPrepaymentAccountReport>(`${this.reportsUrl}/${id}`, { params: reportParams, observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrepaymentAccountReport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryByReportDate(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrepaymentAccountReport[]>(this.reportsUrl, { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrepaymentAccountReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addPrepaymentAccountReportToCollectionIfMissing(
    prepaymentAccountReportCollection: IPrepaymentAccountReport[],
    ...prepaymentAccountReportsToCheck: (IPrepaymentAccountReport | null | undefined)[]
  ): IPrepaymentAccountReport[] {
    const prepaymentAccountReports: IPrepaymentAccountReport[] = prepaymentAccountReportsToCheck.filter(isPresent);
    if (prepaymentAccountReports.length > 0) {
      const prepaymentAccountReportCollectionIdentifiers = prepaymentAccountReportCollection.map(
        prepaymentAccountReportItem => getPrepaymentAccountReportIdentifier(prepaymentAccountReportItem)!
      );
      const prepaymentAccountReportsToAdd = prepaymentAccountReports.filter(prepaymentAccountReportItem => {
        const prepaymentAccountReportIdentifier = getPrepaymentAccountReportIdentifier(prepaymentAccountReportItem);
        if (
          prepaymentAccountReportIdentifier == null ||
          prepaymentAccountReportCollectionIdentifiers.includes(prepaymentAccountReportIdentifier)
        ) {
          return false;
        }
        prepaymentAccountReportCollectionIdentifiers.push(prepaymentAccountReportIdentifier);
        return true;
      });
      return [...prepaymentAccountReportsToAdd, ...prepaymentAccountReportCollection];
    }
    return prepaymentAccountReportCollection;
  }
}
