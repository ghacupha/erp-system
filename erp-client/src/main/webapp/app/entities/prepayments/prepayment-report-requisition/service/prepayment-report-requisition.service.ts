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
import { IPrepaymentReportRequisition, getPrepaymentReportRequisitionIdentifier } from '../prepayment-report-requisition.model';

export type EntityResponseType = HttpResponse<IPrepaymentReportRequisition>;
export type EntityArrayResponseType = HttpResponse<IPrepaymentReportRequisition[]>;

@Injectable({ providedIn: 'root' })
export class PrepaymentReportRequisitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prepayment-report-requisitions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/prepayment-report-requisitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(prepaymentReportRequisition: IPrepaymentReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prepaymentReportRequisition);
    return this.http
      .post<IPrepaymentReportRequisition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(prepaymentReportRequisition: IPrepaymentReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prepaymentReportRequisition);
    return this.http
      .put<IPrepaymentReportRequisition>(
        `${this.resourceUrl}/${getPrepaymentReportRequisitionIdentifier(prepaymentReportRequisition) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(prepaymentReportRequisition: IPrepaymentReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prepaymentReportRequisition);
    return this.http
      .patch<IPrepaymentReportRequisition>(
        `${this.resourceUrl}/${getPrepaymentReportRequisitionIdentifier(prepaymentReportRequisition) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPrepaymentReportRequisition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrepaymentReportRequisition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrepaymentReportRequisition[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPrepaymentReportRequisitionToCollectionIfMissing(
    prepaymentReportRequisitionCollection: IPrepaymentReportRequisition[],
    ...prepaymentReportRequisitionsToCheck: (IPrepaymentReportRequisition | null | undefined)[]
  ): IPrepaymentReportRequisition[] {
    const prepaymentReportRequisitions: IPrepaymentReportRequisition[] = prepaymentReportRequisitionsToCheck.filter(isPresent);
    if (prepaymentReportRequisitions.length > 0) {
      const prepaymentReportRequisitionCollectionIdentifiers = prepaymentReportRequisitionCollection.map(
        prepaymentReportRequisitionItem => getPrepaymentReportRequisitionIdentifier(prepaymentReportRequisitionItem)!
      );
      const prepaymentReportRequisitionsToAdd = prepaymentReportRequisitions.filter(prepaymentReportRequisitionItem => {
        const prepaymentReportRequisitionIdentifier = getPrepaymentReportRequisitionIdentifier(prepaymentReportRequisitionItem);
        if (
          prepaymentReportRequisitionIdentifier == null ||
          prepaymentReportRequisitionCollectionIdentifiers.includes(prepaymentReportRequisitionIdentifier)
        ) {
          return false;
        }
        prepaymentReportRequisitionCollectionIdentifiers.push(prepaymentReportRequisitionIdentifier);
        return true;
      });
      return [...prepaymentReportRequisitionsToAdd, ...prepaymentReportRequisitionCollection];
    }
    return prepaymentReportRequisitionCollection;
  }

  protected convertDateFromClient(prepaymentReportRequisition: IPrepaymentReportRequisition): IPrepaymentReportRequisition {
    return Object.assign({}, prepaymentReportRequisition, {
      reportDate: prepaymentReportRequisition.reportDate?.isValid()
        ? prepaymentReportRequisition.reportDate.format(DATE_FORMAT)
        : undefined,
      timeOfRequisition: prepaymentReportRequisition.timeOfRequisition?.isValid()
        ? prepaymentReportRequisition.timeOfRequisition.toJSON()
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.reportDate = res.body.reportDate ? dayjs(res.body.reportDate) : undefined;
      res.body.timeOfRequisition = res.body.timeOfRequisition ? dayjs(res.body.timeOfRequisition) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((prepaymentReportRequisition: IPrepaymentReportRequisition) => {
        prepaymentReportRequisition.reportDate = prepaymentReportRequisition.reportDate
          ? dayjs(prepaymentReportRequisition.reportDate)
          : undefined;
        prepaymentReportRequisition.timeOfRequisition = prepaymentReportRequisition.timeOfRequisition
          ? dayjs(prepaymentReportRequisition.timeOfRequisition)
          : undefined;
      });
    }
    return res;
  }
}
