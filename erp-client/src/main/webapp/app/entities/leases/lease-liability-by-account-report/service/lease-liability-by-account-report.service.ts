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
import { ILeaseLiabilityByAccountReport, getLeaseLiabilityByAccountReportIdentifier } from '../lease-liability-by-account-report.model';

export type EntityResponseType = HttpResponse<ILeaseLiabilityByAccountReport>;
export type EntityArrayResponseType = HttpResponse<ILeaseLiabilityByAccountReport[]>;

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityByAccountReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lease-liability-by-account-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/lease-liability-by-account-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaseLiabilityByAccountReport: ILeaseLiabilityByAccountReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiabilityByAccountReport);
    return this.http
      .post<ILeaseLiabilityByAccountReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leaseLiabilityByAccountReport: ILeaseLiabilityByAccountReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiabilityByAccountReport);
    return this.http
      .put<ILeaseLiabilityByAccountReport>(
        `${this.resourceUrl}/${getLeaseLiabilityByAccountReportIdentifier(leaseLiabilityByAccountReport) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(leaseLiabilityByAccountReport: ILeaseLiabilityByAccountReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiabilityByAccountReport);
    return this.http
      .patch<ILeaseLiabilityByAccountReport>(
        `${this.resourceUrl}/${getLeaseLiabilityByAccountReportIdentifier(leaseLiabilityByAccountReport) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeaseLiabilityByAccountReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseLiabilityByAccountReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseLiabilityByAccountReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addLeaseLiabilityByAccountReportToCollectionIfMissing(
    leaseLiabilityByAccountReportCollection: ILeaseLiabilityByAccountReport[],
    ...leaseLiabilityByAccountReportsToCheck: (ILeaseLiabilityByAccountReport | null | undefined)[]
  ): ILeaseLiabilityByAccountReport[] {
    const leaseLiabilityByAccountReports: ILeaseLiabilityByAccountReport[] = leaseLiabilityByAccountReportsToCheck.filter(isPresent);
    if (leaseLiabilityByAccountReports.length > 0) {
      const leaseLiabilityByAccountReportCollectionIdentifiers = leaseLiabilityByAccountReportCollection.map(
        leaseLiabilityByAccountReportItem => getLeaseLiabilityByAccountReportIdentifier(leaseLiabilityByAccountReportItem)!
      );
      const leaseLiabilityByAccountReportsToAdd = leaseLiabilityByAccountReports.filter(leaseLiabilityByAccountReportItem => {
        const leaseLiabilityByAccountReportIdentifier = getLeaseLiabilityByAccountReportIdentifier(leaseLiabilityByAccountReportItem);
        if (
          leaseLiabilityByAccountReportIdentifier == null ||
          leaseLiabilityByAccountReportCollectionIdentifiers.includes(leaseLiabilityByAccountReportIdentifier)
        ) {
          return false;
        }
        leaseLiabilityByAccountReportCollectionIdentifiers.push(leaseLiabilityByAccountReportIdentifier);
        return true;
      });
      return [...leaseLiabilityByAccountReportsToAdd, ...leaseLiabilityByAccountReportCollection];
    }
    return leaseLiabilityByAccountReportCollection;
  }

  protected convertDateFromClient(leaseLiabilityByAccountReport: ILeaseLiabilityByAccountReport): ILeaseLiabilityByAccountReport {
    return Object.assign({}, leaseLiabilityByAccountReport, {
      timeOfRequest: leaseLiabilityByAccountReport.timeOfRequest?.isValid()
        ? leaseLiabilityByAccountReport.timeOfRequest.toJSON()
        : undefined,
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
      res.body.forEach((leaseLiabilityByAccountReport: ILeaseLiabilityByAccountReport) => {
        leaseLiabilityByAccountReport.timeOfRequest = leaseLiabilityByAccountReport.timeOfRequest
          ? dayjs(leaseLiabilityByAccountReport.timeOfRequest)
          : undefined;
      });
    }
    return res;
  }
}
