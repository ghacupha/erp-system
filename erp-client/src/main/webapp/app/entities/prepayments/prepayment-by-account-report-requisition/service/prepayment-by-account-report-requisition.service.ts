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
import {
  IPrepaymentByAccountReportRequisition,
  getPrepaymentByAccountReportRequisitionIdentifier,
} from '../prepayment-by-account-report-requisition.model';

export type EntityResponseType = HttpResponse<IPrepaymentByAccountReportRequisition>;
export type EntityArrayResponseType = HttpResponse<IPrepaymentByAccountReportRequisition[]>;

@Injectable({ providedIn: 'root' })
export class PrepaymentByAccountReportRequisitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prepayment-by-account-report-requisitions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/prepayment-by-account-report-requisitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prepaymentByAccountReportRequisition);
    return this.http
      .post<IPrepaymentByAccountReportRequisition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prepaymentByAccountReportRequisition);
    return this.http
      .put<IPrepaymentByAccountReportRequisition>(
        `${this.resourceUrl}/${getPrepaymentByAccountReportRequisitionIdentifier(prepaymentByAccountReportRequisition) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prepaymentByAccountReportRequisition);
    return this.http
      .patch<IPrepaymentByAccountReportRequisition>(
        `${this.resourceUrl}/${getPrepaymentByAccountReportRequisitionIdentifier(prepaymentByAccountReportRequisition) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPrepaymentByAccountReportRequisition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrepaymentByAccountReportRequisition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrepaymentByAccountReportRequisition[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPrepaymentByAccountReportRequisitionToCollectionIfMissing(
    prepaymentByAccountReportRequisitionCollection: IPrepaymentByAccountReportRequisition[],
    ...prepaymentByAccountReportRequisitionsToCheck: (IPrepaymentByAccountReportRequisition | null | undefined)[]
  ): IPrepaymentByAccountReportRequisition[] {
    const prepaymentByAccountReportRequisitions: IPrepaymentByAccountReportRequisition[] =
      prepaymentByAccountReportRequisitionsToCheck.filter(isPresent);
    if (prepaymentByAccountReportRequisitions.length > 0) {
      const prepaymentByAccountReportRequisitionCollectionIdentifiers = prepaymentByAccountReportRequisitionCollection.map(
        prepaymentByAccountReportRequisitionItem =>
          getPrepaymentByAccountReportRequisitionIdentifier(prepaymentByAccountReportRequisitionItem)!
      );
      const prepaymentByAccountReportRequisitionsToAdd = prepaymentByAccountReportRequisitions.filter(
        prepaymentByAccountReportRequisitionItem => {
          const prepaymentByAccountReportRequisitionIdentifier = getPrepaymentByAccountReportRequisitionIdentifier(
            prepaymentByAccountReportRequisitionItem
          );
          if (
            prepaymentByAccountReportRequisitionIdentifier == null ||
            prepaymentByAccountReportRequisitionCollectionIdentifiers.includes(prepaymentByAccountReportRequisitionIdentifier)
          ) {
            return false;
          }
          prepaymentByAccountReportRequisitionCollectionIdentifiers.push(prepaymentByAccountReportRequisitionIdentifier);
          return true;
        }
      );
      return [...prepaymentByAccountReportRequisitionsToAdd, ...prepaymentByAccountReportRequisitionCollection];
    }
    return prepaymentByAccountReportRequisitionCollection;
  }

  protected convertDateFromClient(
    prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition
  ): IPrepaymentByAccountReportRequisition {
    return Object.assign({}, prepaymentByAccountReportRequisition, {
      timeOfRequisition: prepaymentByAccountReportRequisition.timeOfRequisition?.isValid()
        ? prepaymentByAccountReportRequisition.timeOfRequisition.toJSON()
        : undefined,
      reportDate: prepaymentByAccountReportRequisition.reportDate?.isValid()
        ? prepaymentByAccountReportRequisition.reportDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeOfRequisition = res.body.timeOfRequisition ? dayjs(res.body.timeOfRequisition) : undefined;
      res.body.reportDate = res.body.reportDate ? dayjs(res.body.reportDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition) => {
        prepaymentByAccountReportRequisition.timeOfRequisition = prepaymentByAccountReportRequisition.timeOfRequisition
          ? dayjs(prepaymentByAccountReportRequisition.timeOfRequisition)
          : undefined;
        prepaymentByAccountReportRequisition.reportDate = prepaymentByAccountReportRequisition.reportDate
          ? dayjs(prepaymentByAccountReportRequisition.reportDate)
          : undefined;
      });
    }
    return res;
  }
}
