import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import {
  IMonthlyPrepaymentReportRequisition,
  getMonthlyPrepaymentReportRequisitionIdentifier,
} from '../monthly-prepayment-report-requisition.model';

export type EntityResponseType = HttpResponse<IMonthlyPrepaymentReportRequisition>;
export type EntityArrayResponseType = HttpResponse<IMonthlyPrepaymentReportRequisition[]>;

@Injectable({ providedIn: 'root' })
export class MonthlyPrepaymentReportRequisitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/monthly-prepayment-report-requisitions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/monthly-prepayment-report-requisitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(monthlyPrepaymentReportRequisition: IMonthlyPrepaymentReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monthlyPrepaymentReportRequisition);
    return this.http
      .post<IMonthlyPrepaymentReportRequisition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(monthlyPrepaymentReportRequisition: IMonthlyPrepaymentReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monthlyPrepaymentReportRequisition);
    return this.http
      .put<IMonthlyPrepaymentReportRequisition>(
        `${this.resourceUrl}/${getMonthlyPrepaymentReportRequisitionIdentifier(monthlyPrepaymentReportRequisition) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(monthlyPrepaymentReportRequisition: IMonthlyPrepaymentReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monthlyPrepaymentReportRequisition);
    return this.http
      .patch<IMonthlyPrepaymentReportRequisition>(
        `${this.resourceUrl}/${getMonthlyPrepaymentReportRequisitionIdentifier(monthlyPrepaymentReportRequisition) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMonthlyPrepaymentReportRequisition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMonthlyPrepaymentReportRequisition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMonthlyPrepaymentReportRequisition[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addMonthlyPrepaymentReportRequisitionToCollectionIfMissing(
    monthlyPrepaymentReportRequisitionCollection: IMonthlyPrepaymentReportRequisition[],
    ...monthlyPrepaymentReportRequisitionsToCheck: (IMonthlyPrepaymentReportRequisition | null | undefined)[]
  ): IMonthlyPrepaymentReportRequisition[] {
    const monthlyPrepaymentReportRequisitions: IMonthlyPrepaymentReportRequisition[] =
      monthlyPrepaymentReportRequisitionsToCheck.filter(isPresent);
    if (monthlyPrepaymentReportRequisitions.length > 0) {
      const monthlyPrepaymentReportRequisitionCollectionIdentifiers = monthlyPrepaymentReportRequisitionCollection.map(
        monthlyPrepaymentReportRequisitionItem => getMonthlyPrepaymentReportRequisitionIdentifier(monthlyPrepaymentReportRequisitionItem)!
      );
      const monthlyPrepaymentReportRequisitionsToAdd = monthlyPrepaymentReportRequisitions.filter(
        monthlyPrepaymentReportRequisitionItem => {
          const monthlyPrepaymentReportRequisitionIdentifier = getMonthlyPrepaymentReportRequisitionIdentifier(
            monthlyPrepaymentReportRequisitionItem
          );
          if (
            monthlyPrepaymentReportRequisitionIdentifier == null ||
            monthlyPrepaymentReportRequisitionCollectionIdentifiers.includes(monthlyPrepaymentReportRequisitionIdentifier)
          ) {
            return false;
          }
          monthlyPrepaymentReportRequisitionCollectionIdentifiers.push(monthlyPrepaymentReportRequisitionIdentifier);
          return true;
        }
      );
      return [...monthlyPrepaymentReportRequisitionsToAdd, ...monthlyPrepaymentReportRequisitionCollection];
    }
    return monthlyPrepaymentReportRequisitionCollection;
  }

  protected convertDateFromClient(
    monthlyPrepaymentReportRequisition: IMonthlyPrepaymentReportRequisition
  ): IMonthlyPrepaymentReportRequisition {
    return Object.assign({}, monthlyPrepaymentReportRequisition, {
      timeOfRequisition: monthlyPrepaymentReportRequisition.timeOfRequisition?.isValid()
        ? monthlyPrepaymentReportRequisition.timeOfRequisition.toJSON()
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeOfRequisition = res.body.timeOfRequisition ? dayjs(res.body.timeOfRequisition) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((monthlyPrepaymentReportRequisition: IMonthlyPrepaymentReportRequisition) => {
        monthlyPrepaymentReportRequisition.timeOfRequisition = monthlyPrepaymentReportRequisition.timeOfRequisition
          ? dayjs(monthlyPrepaymentReportRequisition.timeOfRequisition)
          : undefined;
      });
    }
    return res;
  }
}
