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
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRouDepreciationEntryReport, getRouDepreciationEntryReportIdentifier } from '../rou-depreciation-entry-report.model';

export type EntityResponseType = HttpResponse<IRouDepreciationEntryReport>;
export type EntityArrayResponseType = HttpResponse<IRouDepreciationEntryReport[]>;

@Injectable({ providedIn: 'root' })
export class RouDepreciationEntryReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/rou-depreciation-entry-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/rou-depreciation-entry-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rouDepreciationEntryReport: IRouDepreciationEntryReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouDepreciationEntryReport);
    return this.http
      .post<IRouDepreciationEntryReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rouDepreciationEntryReport: IRouDepreciationEntryReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouDepreciationEntryReport);
    return this.http
      .put<IRouDepreciationEntryReport>(
        `${this.resourceUrl}/${getRouDepreciationEntryReportIdentifier(rouDepreciationEntryReport) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(rouDepreciationEntryReport: IRouDepreciationEntryReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouDepreciationEntryReport);
    return this.http
      .patch<IRouDepreciationEntryReport>(
        `${this.resourceUrl}/${getRouDepreciationEntryReportIdentifier(rouDepreciationEntryReport) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRouDepreciationEntryReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouDepreciationEntryReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouDepreciationEntryReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addRouDepreciationEntryReportToCollectionIfMissing(
    rouDepreciationEntryReportCollection: IRouDepreciationEntryReport[],
    ...rouDepreciationEntryReportsToCheck: (IRouDepreciationEntryReport | null | undefined)[]
  ): IRouDepreciationEntryReport[] {
    const rouDepreciationEntryReports: IRouDepreciationEntryReport[] = rouDepreciationEntryReportsToCheck.filter(isPresent);
    if (rouDepreciationEntryReports.length > 0) {
      const rouDepreciationEntryReportCollectionIdentifiers = rouDepreciationEntryReportCollection.map(
        rouDepreciationEntryReportItem => getRouDepreciationEntryReportIdentifier(rouDepreciationEntryReportItem)!
      );
      const rouDepreciationEntryReportsToAdd = rouDepreciationEntryReports.filter(rouDepreciationEntryReportItem => {
        const rouDepreciationEntryReportIdentifier = getRouDepreciationEntryReportIdentifier(rouDepreciationEntryReportItem);
        if (
          rouDepreciationEntryReportIdentifier == null ||
          rouDepreciationEntryReportCollectionIdentifiers.includes(rouDepreciationEntryReportIdentifier)
        ) {
          return false;
        }
        rouDepreciationEntryReportCollectionIdentifiers.push(rouDepreciationEntryReportIdentifier);
        return true;
      });
      return [...rouDepreciationEntryReportsToAdd, ...rouDepreciationEntryReportCollection];
    }
    return rouDepreciationEntryReportCollection;
  }

  protected convertDateFromClient(rouDepreciationEntryReport: IRouDepreciationEntryReport): IRouDepreciationEntryReport {
    return Object.assign({}, rouDepreciationEntryReport, {
      timeOfRequest: rouDepreciationEntryReport.timeOfRequest?.isValid() ? rouDepreciationEntryReport.timeOfRequest.toJSON() : undefined,
      periodStartDate: rouDepreciationEntryReport.periodStartDate?.isValid()
        ? rouDepreciationEntryReport.periodStartDate.format(DATE_FORMAT)
        : undefined,
      periodEndDate: rouDepreciationEntryReport.periodEndDate?.isValid()
        ? rouDepreciationEntryReport.periodEndDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeOfRequest = res.body.timeOfRequest ? dayjs(res.body.timeOfRequest) : undefined;
      res.body.periodStartDate = res.body.periodStartDate ? dayjs(res.body.periodStartDate) : undefined;
      res.body.periodEndDate = res.body.periodEndDate ? dayjs(res.body.periodEndDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rouDepreciationEntryReport: IRouDepreciationEntryReport) => {
        rouDepreciationEntryReport.timeOfRequest = rouDepreciationEntryReport.timeOfRequest
          ? dayjs(rouDepreciationEntryReport.timeOfRequest)
          : undefined;
        rouDepreciationEntryReport.periodStartDate = rouDepreciationEntryReport.periodStartDate
          ? dayjs(rouDepreciationEntryReport.periodStartDate)
          : undefined;
        rouDepreciationEntryReport.periodEndDate = rouDepreciationEntryReport.periodEndDate
          ? dayjs(rouDepreciationEntryReport.periodEndDate)
          : undefined;
      });
    }
    return res;
  }
}
