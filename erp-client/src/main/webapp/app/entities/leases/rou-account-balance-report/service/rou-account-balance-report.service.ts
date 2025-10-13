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
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRouAccountBalanceReport, getRouAccountBalanceReportIdentifier } from '../rou-account-balance-report.model';

export type EntityResponseType = HttpResponse<IRouAccountBalanceReport>;
export type EntityArrayResponseType = HttpResponse<IRouAccountBalanceReport[]>;

@Injectable({ providedIn: 'root' })
export class RouAccountBalanceReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rou-account-balance-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/rou-account-balance-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rouAccountBalanceReport: IRouAccountBalanceReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouAccountBalanceReport);
    return this.http
      .post<IRouAccountBalanceReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rouAccountBalanceReport: IRouAccountBalanceReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouAccountBalanceReport);
    return this.http
      .put<IRouAccountBalanceReport>(
        `${this.resourceUrl}/${getRouAccountBalanceReportIdentifier(rouAccountBalanceReport) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(rouAccountBalanceReport: IRouAccountBalanceReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouAccountBalanceReport);
    return this.http
      .patch<IRouAccountBalanceReport>(
        `${this.resourceUrl}/${getRouAccountBalanceReportIdentifier(rouAccountBalanceReport) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRouAccountBalanceReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouAccountBalanceReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouAccountBalanceReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addRouAccountBalanceReportToCollectionIfMissing(
    rouAccountBalanceReportCollection: IRouAccountBalanceReport[],
    ...rouAccountBalanceReportsToCheck: (IRouAccountBalanceReport | null | undefined)[]
  ): IRouAccountBalanceReport[] {
    const rouAccountBalanceReports: IRouAccountBalanceReport[] = rouAccountBalanceReportsToCheck.filter(isPresent);
    if (rouAccountBalanceReports.length > 0) {
      const rouAccountBalanceReportCollectionIdentifiers = rouAccountBalanceReportCollection.map(
        rouAccountBalanceReportItem => getRouAccountBalanceReportIdentifier(rouAccountBalanceReportItem)!
      );
      const rouAccountBalanceReportsToAdd = rouAccountBalanceReports.filter(rouAccountBalanceReportItem => {
        const rouAccountBalanceReportIdentifier = getRouAccountBalanceReportIdentifier(rouAccountBalanceReportItem);
        if (
          rouAccountBalanceReportIdentifier == null ||
          rouAccountBalanceReportCollectionIdentifiers.includes(rouAccountBalanceReportIdentifier)
        ) {
          return false;
        }
        rouAccountBalanceReportCollectionIdentifiers.push(rouAccountBalanceReportIdentifier);
        return true;
      });
      return [...rouAccountBalanceReportsToAdd, ...rouAccountBalanceReportCollection];
    }
    return rouAccountBalanceReportCollection;
  }

  protected convertDateFromClient(rouAccountBalanceReport: IRouAccountBalanceReport): IRouAccountBalanceReport {
    return Object.assign({}, rouAccountBalanceReport, {
      timeOfRequest: rouAccountBalanceReport.timeOfRequest?.isValid() ? rouAccountBalanceReport.timeOfRequest.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeOfRequest = res.body.timeOfRequest ? dayjs(res.body.timeOfRequest) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rouAccountBalanceReport: IRouAccountBalanceReport) => {
        rouAccountBalanceReport.timeOfRequest = rouAccountBalanceReport.timeOfRequest
          ? dayjs(rouAccountBalanceReport.timeOfRequest)
          : undefined;
      });
    }
    return res;
  }
}
