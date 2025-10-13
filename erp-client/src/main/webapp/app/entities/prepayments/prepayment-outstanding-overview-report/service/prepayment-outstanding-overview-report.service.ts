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
import {
  IPrepaymentOutstandingOverviewReport,
  getPrepaymentOutstandingOverviewReportIdentifier,
} from '../prepayment-outstanding-overview-report.model';

export type EntityResponseType = HttpResponse<IPrepaymentOutstandingOverviewReport>;
export type EntityArrayResponseType = HttpResponse<IPrepaymentOutstandingOverviewReport[]>;

@Injectable({ providedIn: 'root' })
export class PrepaymentOutstandingOverviewReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prepayment-outstanding-overview-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/prepayment-outstanding-overview-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrepaymentOutstandingOverviewReport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrepaymentOutstandingOverviewReport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrepaymentOutstandingOverviewReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addPrepaymentOutstandingOverviewReportToCollectionIfMissing(
    prepaymentOutstandingOverviewReportCollection: IPrepaymentOutstandingOverviewReport[],
    ...prepaymentOutstandingOverviewReportsToCheck: (IPrepaymentOutstandingOverviewReport | null | undefined)[]
  ): IPrepaymentOutstandingOverviewReport[] {
    const prepaymentOutstandingOverviewReports: IPrepaymentOutstandingOverviewReport[] =
      prepaymentOutstandingOverviewReportsToCheck.filter(isPresent);
    if (prepaymentOutstandingOverviewReports.length > 0) {
      const prepaymentOutstandingOverviewReportCollectionIdentifiers = prepaymentOutstandingOverviewReportCollection.map(
        prepaymentOutstandingOverviewReportItem =>
          getPrepaymentOutstandingOverviewReportIdentifier(prepaymentOutstandingOverviewReportItem)!
      );
      const prepaymentOutstandingOverviewReportsToAdd = prepaymentOutstandingOverviewReports.filter(
        prepaymentOutstandingOverviewReportItem => {
          const prepaymentOutstandingOverviewReportIdentifier = getPrepaymentOutstandingOverviewReportIdentifier(
            prepaymentOutstandingOverviewReportItem
          );
          if (
            prepaymentOutstandingOverviewReportIdentifier == null ||
            prepaymentOutstandingOverviewReportCollectionIdentifiers.includes(prepaymentOutstandingOverviewReportIdentifier)
          ) {
            return false;
          }
          prepaymentOutstandingOverviewReportCollectionIdentifiers.push(prepaymentOutstandingOverviewReportIdentifier);
          return true;
        }
      );
      return [...prepaymentOutstandingOverviewReportsToAdd, ...prepaymentOutstandingOverviewReportCollection];
    }
    return prepaymentOutstandingOverviewReportCollection;
  }
}
