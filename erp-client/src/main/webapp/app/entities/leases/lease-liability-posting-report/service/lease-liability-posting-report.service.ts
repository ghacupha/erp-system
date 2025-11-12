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
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILeaseLiabilityPostingReport, getLeaseLiabilityPostingReportIdentifier } from '../lease-liability-posting-report.model';

export type EntityResponseType = HttpResponse<ILeaseLiabilityPostingReport>;
export type EntityArrayResponseType = HttpResponse<ILeaseLiabilityPostingReport[]>;

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityPostingReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lease-liability-posting-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/lease-liability-posting-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaseLiabilityPostingReport: ILeaseLiabilityPostingReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiabilityPostingReport);
    return this.http
      .post<ILeaseLiabilityPostingReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leaseLiabilityPostingReport: ILeaseLiabilityPostingReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiabilityPostingReport);
    return this.http
      .put<ILeaseLiabilityPostingReport>(
        `${this.resourceUrl}/${getLeaseLiabilityPostingReportIdentifier(leaseLiabilityPostingReport) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(leaseLiabilityPostingReport: ILeaseLiabilityPostingReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiabilityPostingReport);
    return this.http
      .patch<ILeaseLiabilityPostingReport>(
        `${this.resourceUrl}/${getLeaseLiabilityPostingReportIdentifier(leaseLiabilityPostingReport) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeaseLiabilityPostingReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseLiabilityPostingReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseLiabilityPostingReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addLeaseLiabilityPostingReportToCollectionIfMissing(
    leaseLiabilityPostingReportCollection: ILeaseLiabilityPostingReport[],
    ...leaseLiabilityPostingReportsToCheck: (ILeaseLiabilityPostingReport | null | undefined)[]
  ): ILeaseLiabilityPostingReport[] {
    const leaseLiabilityPostingReports: ILeaseLiabilityPostingReport[] = leaseLiabilityPostingReportsToCheck.filter(isPresent);
    if (leaseLiabilityPostingReports.length > 0) {
      const leaseLiabilityPostingReportCollectionIdentifiers = leaseLiabilityPostingReportCollection.map(
        leaseLiabilityPostingReportItem => getLeaseLiabilityPostingReportIdentifier(leaseLiabilityPostingReportItem)!
      );
      const leaseLiabilityPostingReportsToAdd = leaseLiabilityPostingReports.filter(leaseLiabilityPostingReportItem => {
        const leaseLiabilityPostingReportIdentifier = getLeaseLiabilityPostingReportIdentifier(leaseLiabilityPostingReportItem);
        if (
          leaseLiabilityPostingReportIdentifier == null ||
          leaseLiabilityPostingReportCollectionIdentifiers.includes(leaseLiabilityPostingReportIdentifier)
        ) {
          return false;
        }
        leaseLiabilityPostingReportCollectionIdentifiers.push(leaseLiabilityPostingReportIdentifier);
        return true;
      });
      return [...leaseLiabilityPostingReportsToAdd, ...leaseLiabilityPostingReportCollection];
    }
    return leaseLiabilityPostingReportCollection;
  }

  protected convertDateFromClient(leaseLiabilityPostingReport: ILeaseLiabilityPostingReport): ILeaseLiabilityPostingReport {
    return Object.assign({}, leaseLiabilityPostingReport, {
      timeOfRequest: leaseLiabilityPostingReport.timeOfRequest?.isValid() ? leaseLiabilityPostingReport.timeOfRequest.toJSON() : undefined,
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
      res.body.forEach((leaseLiabilityPostingReport: ILeaseLiabilityPostingReport) => {
        leaseLiabilityPostingReport.timeOfRequest = leaseLiabilityPostingReport.timeOfRequest
          ? dayjs(leaseLiabilityPostingReport.timeOfRequest)
          : undefined;
      });
    }
    return res;
  }
}
