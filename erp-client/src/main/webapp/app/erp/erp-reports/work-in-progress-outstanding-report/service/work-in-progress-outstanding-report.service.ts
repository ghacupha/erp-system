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
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import {
  IWorkInProgressOutstandingReport,
  getWorkInProgressOutstandingReportIdentifier,
} from '../work-in-progress-outstanding-report.model';

export type EntityResponseType = HttpResponse<IWorkInProgressOutstandingReport>;
export type EntityArrayResponseType = HttpResponse<IWorkInProgressOutstandingReport[]>;

@Injectable({ providedIn: 'root' })
export class WorkInProgressOutstandingReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/work-in-progress-outstanding-reports');
  protected reportsUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/work-in-progress-outstanding-reports/reported');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/_search/work-in-progress-outstanding-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkInProgressOutstandingReport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByDate(reportDate: string, id: number): Observable<EntityResponseType> {
    let reportParams: HttpParams = new HttpParams();
    reportParams = reportParams.set('reportDate', reportDate);
    return this.http.get<IWorkInProgressOutstandingReport>(`${this.reportsUrl}/${id}`, { params: reportParams, observe: 'response' })
      .pipe(map((res) => this.convertDateFromServer(res)));
  }

  queryByReportDate(reportDate: dayjs.Dayjs, req?: any): Observable<EntityArrayResponseType> {
    let options = createRequestOption(req);
    options = options.set('reportDate', this.convertDateItemFromClient(reportDate))

    return this.http.get<IWorkInProgressOutstandingReport[]>(this.resourceUrl + `/reported`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorkInProgressOutstandingReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorkInProgressOutstandingReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addWorkInProgressOutstandingReportToCollectionIfMissing(
    workInProgressOutstandingReportCollection: IWorkInProgressOutstandingReport[],
    ...workInProgressOutstandingReportsToCheck: (IWorkInProgressOutstandingReport | null | undefined)[]
  ): IWorkInProgressOutstandingReport[] {
    const workInProgressOutstandingReports: IWorkInProgressOutstandingReport[] = workInProgressOutstandingReportsToCheck.filter(isPresent);
    if (workInProgressOutstandingReports.length > 0) {
      const workInProgressOutstandingReportCollectionIdentifiers = workInProgressOutstandingReportCollection.map(
        workInProgressOutstandingReportItem => getWorkInProgressOutstandingReportIdentifier(workInProgressOutstandingReportItem)!
      );
      const workInProgressOutstandingReportsToAdd = workInProgressOutstandingReports.filter(workInProgressOutstandingReportItem => {
        const workInProgressOutstandingReportIdentifier = getWorkInProgressOutstandingReportIdentifier(workInProgressOutstandingReportItem);
        if (
          workInProgressOutstandingReportIdentifier == null ||
          workInProgressOutstandingReportCollectionIdentifiers.includes(workInProgressOutstandingReportIdentifier)
        ) {
          return false;
        }
        workInProgressOutstandingReportCollectionIdentifiers.push(workInProgressOutstandingReportIdentifier);
        return true;
      });
      return [...workInProgressOutstandingReportsToAdd, ...workInProgressOutstandingReportCollection];
    }
    return workInProgressOutstandingReportCollection;
  }

  convertDateItemFromClient(dateItem: dayjs.Dayjs): string {
    return dateItem.isValid() ? dateItem.format(DATE_FORMAT) : dayjs().format(DATE_FORMAT);
  }

  protected convertDateFromClient(workInProgressOutstandingReport: IWorkInProgressOutstandingReport): IWorkInProgressOutstandingReport {
    return Object.assign({}, workInProgressOutstandingReport, {
      instalmentTransactionDate: workInProgressOutstandingReport.instalmentTransactionDate?.isValid()
        ? workInProgressOutstandingReport.instalmentTransactionDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.instalmentTransactionDate = res.body.instalmentTransactionDate ? dayjs(res.body.instalmentTransactionDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((workInProgressOutstandingReport: IWorkInProgressOutstandingReport) => {
        workInProgressOutstandingReport.instalmentTransactionDate = workInProgressOutstandingReport.instalmentTransactionDate
          ? dayjs(workInProgressOutstandingReport.instalmentTransactionDate)
          : undefined;
      });
    }
    return res;
  }
}
