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
  IWorkInProgressOutstandingReport,
  getWorkInProgressOutstandingReportIdentifier,
} from '../work-in-progress-outstanding-report.model';

export type EntityResponseType = HttpResponse<IWorkInProgressOutstandingReport>;
export type EntityArrayResponseType = HttpResponse<IWorkInProgressOutstandingReport[]>;

@Injectable({ providedIn: 'root' })
export class WorkInProgressOutstandingReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/work-in-progress-outstanding-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/work-in-progress-outstanding-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWorkInProgressOutstandingReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorkInProgressOutstandingReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorkInProgressOutstandingReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addWorkInProgressOutstandingReportToCollectionIfMissing(
    workInProgressOutstandingReportCollection: IWorkInProgressOutstandingReport[],
    ...workInProgressOutstandingReportsToCheck: (IWorkInProgressOutstandingReport | null | undefined)[]
  ): IWorkInProgressOutstandingReport[] {
    const workInProgressOutstandingReports: IWorkInProgressOutstandingReport[] = workInProgressOutstandingReportsToCheck.filter(isPresent);
    if (workInProgressOutstandingReports.length > 0) {
      const workInProgressOutstandingReportCollectionIdentifiers = workInProgressOutstandingReportCollection.map(
        workInProgressOutstandingReportItem => getWorkInProgressOutstandingReportIdentifier(workInProgressOutstandingReportItem)!
      );
      const workInProgressOutstandingReportsToAdd = workInProgressOutstandingReports.filter(workInProgressOutstandingReportItem => {
        const workInProgressOutstandingReportIdentifier = getWorkInProgressOutstandingReportIdentifier(workInProgressOutstandingReportItem);
        if (
          workInProgressOutstandingReportIdentifier == null ||
          workInProgressOutstandingReportCollectionIdentifiers.includes(workInProgressOutstandingReportIdentifier)
        ) {
          return false;
        }
        workInProgressOutstandingReportCollectionIdentifiers.push(workInProgressOutstandingReportIdentifier);
        return true;
      });
      return [...workInProgressOutstandingReportsToAdd, ...workInProgressOutstandingReportCollection];
    }
    return workInProgressOutstandingReportCollection;
  }

  protected convertDateFromClient(workInProgressOutstandingReport: IWorkInProgressOutstandingReport): IWorkInProgressOutstandingReport {
    return Object.assign({}, workInProgressOutstandingReport, {
      instalmentTransactionDate: workInProgressOutstandingReport.instalmentTransactionDate?.isValid()
        ? workInProgressOutstandingReport.instalmentTransactionDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.instalmentTransactionDate = res.body.instalmentTransactionDate ? dayjs(res.body.instalmentTransactionDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((workInProgressOutstandingReport: IWorkInProgressOutstandingReport) => {
        workInProgressOutstandingReport.instalmentTransactionDate = workInProgressOutstandingReport.instalmentTransactionDate
          ? dayjs(workInProgressOutstandingReport.instalmentTransactionDate)
          : undefined;
      });
    }
    return res;
  }
}
