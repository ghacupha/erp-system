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
  IWorkInProgressOutstandingReportRequisition,
  getWorkInProgressOutstandingReportRequisitionIdentifier,
} from '../work-in-progress-outstanding-report-requisition.model';

export type EntityResponseType = HttpResponse<IWorkInProgressOutstandingReportRequisition>;
export type EntityArrayResponseType = HttpResponse<IWorkInProgressOutstandingReportRequisition[]>;

@Injectable({ providedIn: 'root' })
export class WorkInProgressOutstandingReportRequisitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/work-in-progress-outstanding-report-requisitions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/_search/work-in-progress-outstanding-report-requisitions'
  );

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(workInProgressOutstandingReportRequisition: IWorkInProgressOutstandingReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workInProgressOutstandingReportRequisition);
    return this.http
      .post<IWorkInProgressOutstandingReportRequisition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(workInProgressOutstandingReportRequisition: IWorkInProgressOutstandingReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workInProgressOutstandingReportRequisition);
    return this.http
      .put<IWorkInProgressOutstandingReportRequisition>(
        `${this.resourceUrl}/${
          getWorkInProgressOutstandingReportRequisitionIdentifier(workInProgressOutstandingReportRequisition) as number
        }`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(workInProgressOutstandingReportRequisition: IWorkInProgressOutstandingReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workInProgressOutstandingReportRequisition);
    return this.http
      .patch<IWorkInProgressOutstandingReportRequisition>(
        `${this.resourceUrl}/${
          getWorkInProgressOutstandingReportRequisitionIdentifier(workInProgressOutstandingReportRequisition) as number
        }`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWorkInProgressOutstandingReportRequisition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorkInProgressOutstandingReportRequisition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorkInProgressOutstandingReportRequisition[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addWorkInProgressOutstandingReportRequisitionToCollectionIfMissing(
    workInProgressOutstandingReportRequisitionCollection: IWorkInProgressOutstandingReportRequisition[],
    ...workInProgressOutstandingReportRequisitionsToCheck: (IWorkInProgressOutstandingReportRequisition | null | undefined)[]
  ): IWorkInProgressOutstandingReportRequisition[] {
    const workInProgressOutstandingReportRequisitions: IWorkInProgressOutstandingReportRequisition[] =
      workInProgressOutstandingReportRequisitionsToCheck.filter(isPresent);
    if (workInProgressOutstandingReportRequisitions.length > 0) {
      const workInProgressOutstandingReportRequisitionCollectionIdentifiers = workInProgressOutstandingReportRequisitionCollection.map(
        workInProgressOutstandingReportRequisitionItem =>
          getWorkInProgressOutstandingReportRequisitionIdentifier(workInProgressOutstandingReportRequisitionItem)!
      );
      const workInProgressOutstandingReportRequisitionsToAdd = workInProgressOutstandingReportRequisitions.filter(
        workInProgressOutstandingReportRequisitionItem => {
          const workInProgressOutstandingReportRequisitionIdentifier = getWorkInProgressOutstandingReportRequisitionIdentifier(
            workInProgressOutstandingReportRequisitionItem
          );
          if (
            workInProgressOutstandingReportRequisitionIdentifier == null ||
            workInProgressOutstandingReportRequisitionCollectionIdentifiers.includes(workInProgressOutstandingReportRequisitionIdentifier)
          ) {
            return false;
          }
          workInProgressOutstandingReportRequisitionCollectionIdentifiers.push(workInProgressOutstandingReportRequisitionIdentifier);
          return true;
        }
      );
      return [...workInProgressOutstandingReportRequisitionsToAdd, ...workInProgressOutstandingReportRequisitionCollection];
    }
    return workInProgressOutstandingReportRequisitionCollection;
  }

  protected convertDateFromClient(
    workInProgressOutstandingReportRequisition: IWorkInProgressOutstandingReportRequisition
  ): IWorkInProgressOutstandingReportRequisition {
    return Object.assign({}, workInProgressOutstandingReportRequisition, {
      reportDate: workInProgressOutstandingReportRequisition.reportDate?.isValid()
        ? workInProgressOutstandingReportRequisition.reportDate.format(DATE_FORMAT)
        : undefined,
      timeOfRequisition: workInProgressOutstandingReportRequisition.timeOfRequisition?.isValid()
        ? workInProgressOutstandingReportRequisition.timeOfRequisition.toJSON()
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
      res.body.forEach((workInProgressOutstandingReportRequisition: IWorkInProgressOutstandingReportRequisition) => {
        workInProgressOutstandingReportRequisition.reportDate = workInProgressOutstandingReportRequisition.reportDate
          ? dayjs(workInProgressOutstandingReportRequisition.reportDate)
          : undefined;
        workInProgressOutstandingReportRequisition.timeOfRequisition = workInProgressOutstandingReportRequisition.timeOfRequisition
          ? dayjs(workInProgressOutstandingReportRequisition.timeOfRequisition)
          : undefined;
      });
    }
    return res;
  }
}
