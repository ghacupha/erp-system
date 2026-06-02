import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILeaseLiabilityScheduleReport, getLeaseLiabilityScheduleReportIdentifier } from '../lease-liability-schedule-report.model';

export type EntityResponseType = HttpResponse<ILeaseLiabilityScheduleReport>;
export type EntityArrayResponseType = HttpResponse<ILeaseLiabilityScheduleReport[]>;

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityScheduleReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lease-liability-schedule-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/lease-liability-schedule-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiabilityScheduleReport);
    return this.http
      .post<ILeaseLiabilityScheduleReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiabilityScheduleReport);
    return this.http
      .put<ILeaseLiabilityScheduleReport>(
        `${this.resourceUrl}/${getLeaseLiabilityScheduleReportIdentifier(leaseLiabilityScheduleReport) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(leaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiabilityScheduleReport);
    return this.http
      .patch<ILeaseLiabilityScheduleReport>(
        `${this.resourceUrl}/${getLeaseLiabilityScheduleReportIdentifier(leaseLiabilityScheduleReport) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeaseLiabilityScheduleReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseLiabilityScheduleReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseLiabilityScheduleReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addLeaseLiabilityScheduleReportToCollectionIfMissing(
    leaseLiabilityScheduleReportCollection: ILeaseLiabilityScheduleReport[],
    ...leaseLiabilityScheduleReportsToCheck: (ILeaseLiabilityScheduleReport | null | undefined)[]
  ): ILeaseLiabilityScheduleReport[] {
    const leaseLiabilityScheduleReports: ILeaseLiabilityScheduleReport[] = leaseLiabilityScheduleReportsToCheck.filter(isPresent);
    if (leaseLiabilityScheduleReports.length > 0) {
      const leaseLiabilityScheduleReportCollectionIdentifiers = leaseLiabilityScheduleReportCollection.map(
        leaseLiabilityScheduleReportItem => getLeaseLiabilityScheduleReportIdentifier(leaseLiabilityScheduleReportItem)!
      );
      const leaseLiabilityScheduleReportsToAdd = leaseLiabilityScheduleReports.filter(leaseLiabilityScheduleReportItem => {
        const leaseLiabilityScheduleReportIdentifier = getLeaseLiabilityScheduleReportIdentifier(leaseLiabilityScheduleReportItem);
        if (
          leaseLiabilityScheduleReportIdentifier == null ||
          leaseLiabilityScheduleReportCollectionIdentifiers.includes(leaseLiabilityScheduleReportIdentifier)
        ) {
          return false;
        }
        leaseLiabilityScheduleReportCollectionIdentifiers.push(leaseLiabilityScheduleReportIdentifier);
        return true;
      });
      return [...leaseLiabilityScheduleReportsToAdd, ...leaseLiabilityScheduleReportCollection];
    }
    return leaseLiabilityScheduleReportCollection;
  }

  protected convertDateFromClient(leaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport): ILeaseLiabilityScheduleReport {
    return Object.assign({}, leaseLiabilityScheduleReport, {
      timeOfRequest: leaseLiabilityScheduleReport.timeOfRequest?.isValid()
        ? leaseLiabilityScheduleReport.timeOfRequest.toJSON()
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
      res.body.forEach((leaseLiabilityScheduleReport: ILeaseLiabilityScheduleReport) => {
        leaseLiabilityScheduleReport.timeOfRequest = leaseLiabilityScheduleReport.timeOfRequest
          ? dayjs(leaseLiabilityScheduleReport.timeOfRequest)
          : undefined;
      });
    }
    return res;
  }
}
