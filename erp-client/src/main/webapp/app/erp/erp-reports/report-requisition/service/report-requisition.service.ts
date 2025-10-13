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
import { IReportRequisition, getReportRequisitionIdentifier } from '../report-requisition.model';
import { sha512 } from 'hash-wasm';

export type EntityResponseType = HttpResponse<IReportRequisition>;
export type EntityArrayResponseType = HttpResponse<IReportRequisition[]>;

@Injectable({ providedIn: 'root' })
export class ReportRequisitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/app/report-requisitions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/app/_search/report-requisitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(reportRequisition: IReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportRequisition);
    return this.http
      .post<IReportRequisition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)))
      .pipe(map((res: EntityResponseType) => this.convertPasswordFromServer(res)));
  }

  update(reportRequisition: IReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportRequisition);
    return this.http
      .put<IReportRequisition>(`${this.resourceUrl}/${getReportRequisitionIdentifier(reportRequisition) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)))
      .pipe(map((res: EntityResponseType) => this.convertPasswordFromServer(res)));
  }

  partialUpdate(reportRequisition: IReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reportRequisition);
    return this.http
      .patch<IReportRequisition>(`${this.resourceUrl}/${getReportRequisitionIdentifier(reportRequisition) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)))
      .pipe(map((res: EntityResponseType) => this.convertPasswordFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IReportRequisition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)))
      .pipe(map((res: EntityResponseType) => this.convertPasswordFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReportRequisition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)))
      .pipe(map((res: EntityArrayResponseType) => this.convertPasswordArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReportRequisition[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)))
      .pipe(map((res: EntityArrayResponseType) => this.convertPasswordArrayFromServer(res)));
  }

  addReportRequisitionToCollectionIfMissing(
    reportRequisitionCollection: IReportRequisition[],
    ...reportRequisitionsToCheck: (IReportRequisition | null | undefined)[]
  ): IReportRequisition[] {
    const reportRequisitions: IReportRequisition[] = reportRequisitionsToCheck.filter(isPresent);
    if (reportRequisitions.length > 0) {
      const reportRequisitionCollectionIdentifiers = reportRequisitionCollection.map(
        reportRequisitionItem => getReportRequisitionIdentifier(reportRequisitionItem)!
      );
      const reportRequisitionsToAdd = reportRequisitions.filter(reportRequisitionItem => {
        const reportRequisitionIdentifier = getReportRequisitionIdentifier(reportRequisitionItem);
        if (reportRequisitionIdentifier == null || reportRequisitionCollectionIdentifiers.includes(reportRequisitionIdentifier)) {
          return false;
        }
        reportRequisitionCollectionIdentifiers.push(reportRequisitionIdentifier);
        return true;
      });
      return [...reportRequisitionsToAdd, ...reportRequisitionCollection];
    }
    return reportRequisitionCollection;
  }

  protected convertDateFromClient(reportRequisition: IReportRequisition): IReportRequisition {
    return Object.assign({}, reportRequisition, {
      reportRequestTime: reportRequisition.reportRequestTime?.isValid() ? reportRequisition.reportRequestTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.reportRequestTime = res.body.reportRequestTime ? dayjs(res.body.reportRequestTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((reportRequisition: IReportRequisition) => {
        reportRequisition.reportRequestTime = reportRequisition.reportRequestTime ? dayjs(reportRequisition.reportRequestTime) : undefined;
      });
    }
    return res;
  }

  protected convertPasswordFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      if (res.body.reportPassword) {
        sha512(res.body.reportPassword).then(token => {
          res.body!.reportPassword = token.substring(0,15)
        })
      }
      if (res.body.reportPassword) {
        sha512(res.body.reportPassword).then(token => {
          res.body!.reportPassword = token.substring(0,15)
        })
      }
    }
    return res;
  }

  protected convertPasswordArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((reportRequisition: IReportRequisition) => {
        if (reportRequisition.reportPassword) {
          sha512(reportRequisition.reportPassword).then(token => {
            reportRequisition.reportPassword = token.substring(0,15)
          })
        }
      });
    }
    return res;
  }
}
