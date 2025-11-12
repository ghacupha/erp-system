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
import { ILeaseLiabilityReport, getLeaseLiabilityReportIdentifier } from '../lease-liability-report.model';

export type EntityResponseType = HttpResponse<ILeaseLiabilityReport>;
export type EntityArrayResponseType = HttpResponse<ILeaseLiabilityReport[]>;

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/lease-liability-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/lease-liability-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaseLiabilityReport: ILeaseLiabilityReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiabilityReport);
    return this.http
      .post<ILeaseLiabilityReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leaseLiabilityReport: ILeaseLiabilityReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiabilityReport);
    return this.http
      .put<ILeaseLiabilityReport>(`${this.resourceUrl}/${getLeaseLiabilityReportIdentifier(leaseLiabilityReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(leaseLiabilityReport: ILeaseLiabilityReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiabilityReport);
    return this.http
      .patch<ILeaseLiabilityReport>(`${this.resourceUrl}/${getLeaseLiabilityReportIdentifier(leaseLiabilityReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeaseLiabilityReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseLiabilityReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseLiabilityReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addLeaseLiabilityReportToCollectionIfMissing(
    leaseLiabilityReportCollection: ILeaseLiabilityReport[],
    ...leaseLiabilityReportsToCheck: (ILeaseLiabilityReport | null | undefined)[]
  ): ILeaseLiabilityReport[] {
    const leaseLiabilityReports: ILeaseLiabilityReport[] = leaseLiabilityReportsToCheck.filter(isPresent);
    if (leaseLiabilityReports.length > 0) {
      const leaseLiabilityReportCollectionIdentifiers = leaseLiabilityReportCollection.map(
        leaseLiabilityReportItem => getLeaseLiabilityReportIdentifier(leaseLiabilityReportItem)!
      );
      const leaseLiabilityReportsToAdd = leaseLiabilityReports.filter(leaseLiabilityReportItem => {
        const leaseLiabilityReportIdentifier = getLeaseLiabilityReportIdentifier(leaseLiabilityReportItem);
        if (leaseLiabilityReportIdentifier == null || leaseLiabilityReportCollectionIdentifiers.includes(leaseLiabilityReportIdentifier)) {
          return false;
        }
        leaseLiabilityReportCollectionIdentifiers.push(leaseLiabilityReportIdentifier);
        return true;
      });
      return [...leaseLiabilityReportsToAdd, ...leaseLiabilityReportCollection];
    }
    return leaseLiabilityReportCollection;
  }

  protected convertDateFromClient(leaseLiabilityReport: ILeaseLiabilityReport): ILeaseLiabilityReport {
    return Object.assign({}, leaseLiabilityReport, {
      timeOfRequest: leaseLiabilityReport.timeOfRequest?.isValid() ? leaseLiabilityReport.timeOfRequest.toJSON() : undefined,
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
      res.body.forEach((leaseLiabilityReport: ILeaseLiabilityReport) => {
        leaseLiabilityReport.timeOfRequest = leaseLiabilityReport.timeOfRequest ? dayjs(leaseLiabilityReport.timeOfRequest) : undefined;
      });
    }
    return res;
  }
}
