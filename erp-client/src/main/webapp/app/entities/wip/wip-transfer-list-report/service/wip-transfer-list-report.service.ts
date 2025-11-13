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
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IWIPTransferListReport, getWIPTransferListReportIdentifier } from '../wip-transfer-list-report.model';

export type EntityResponseType = HttpResponse<IWIPTransferListReport>;
export type EntityArrayResponseType = HttpResponse<IWIPTransferListReport[]>;

@Injectable({ providedIn: 'root' })
export class WIPTransferListReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/wip-transfer-list-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/wip-transfer-list-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(wIPTransferListReport: IWIPTransferListReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wIPTransferListReport);
    return this.http
      .post<IWIPTransferListReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(wIPTransferListReport: IWIPTransferListReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wIPTransferListReport);
    return this.http
      .put<IWIPTransferListReport>(`${this.resourceUrl}/${getWIPTransferListReportIdentifier(wIPTransferListReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(wIPTransferListReport: IWIPTransferListReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wIPTransferListReport);
    return this.http
      .patch<IWIPTransferListReport>(`${this.resourceUrl}/${getWIPTransferListReportIdentifier(wIPTransferListReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWIPTransferListReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWIPTransferListReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWIPTransferListReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addWIPTransferListReportToCollectionIfMissing(
    wIPTransferListReportCollection: IWIPTransferListReport[],
    ...wIPTransferListReportsToCheck: (IWIPTransferListReport | null | undefined)[]
  ): IWIPTransferListReport[] {
    const wIPTransferListReports: IWIPTransferListReport[] = wIPTransferListReportsToCheck.filter(isPresent);
    if (wIPTransferListReports.length > 0) {
      const wIPTransferListReportCollectionIdentifiers = wIPTransferListReportCollection.map(
        wIPTransferListReportItem => getWIPTransferListReportIdentifier(wIPTransferListReportItem)!
      );
      const wIPTransferListReportsToAdd = wIPTransferListReports.filter(wIPTransferListReportItem => {
        const wIPTransferListReportIdentifier = getWIPTransferListReportIdentifier(wIPTransferListReportItem);
        if (
          wIPTransferListReportIdentifier == null ||
          wIPTransferListReportCollectionIdentifiers.includes(wIPTransferListReportIdentifier)
        ) {
          return false;
        }
        wIPTransferListReportCollectionIdentifiers.push(wIPTransferListReportIdentifier);
        return true;
      });
      return [...wIPTransferListReportsToAdd, ...wIPTransferListReportCollection];
    }
    return wIPTransferListReportCollection;
  }

  protected convertDateFromClient(wIPTransferListReport: IWIPTransferListReport): IWIPTransferListReport {
    return Object.assign({}, wIPTransferListReport, {
      timeOfRequest: wIPTransferListReport.timeOfRequest?.isValid() ? wIPTransferListReport.timeOfRequest.toJSON() : undefined,
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
      res.body.forEach((wIPTransferListReport: IWIPTransferListReport) => {
        wIPTransferListReport.timeOfRequest = wIPTransferListReport.timeOfRequest ? dayjs(wIPTransferListReport.timeOfRequest) : undefined;
      });
    }
    return res;
  }
}
