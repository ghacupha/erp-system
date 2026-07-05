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
import { IPrepaymentReport, getPrepaymentReportIdentifier } from '../prepayment-report.model';

export type EntityResponseType = HttpResponse<IPrepaymentReport>;
export type EntityArrayResponseType = HttpResponse<IPrepaymentReport[]>;

@Injectable({ providedIn: 'root' })
export class PrepaymentReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prepayment-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/prepayment-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPrepaymentReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrepaymentReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrepaymentReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPrepaymentReportToCollectionIfMissing(
    prepaymentReportCollection: IPrepaymentReport[],
    ...prepaymentReportsToCheck: (IPrepaymentReport | null | undefined)[]
  ): IPrepaymentReport[] {
    const prepaymentReports: IPrepaymentReport[] = prepaymentReportsToCheck.filter(isPresent);
    if (prepaymentReports.length > 0) {
      const prepaymentReportCollectionIdentifiers = prepaymentReportCollection.map(
        prepaymentReportItem => getPrepaymentReportIdentifier(prepaymentReportItem)!
      );
      const prepaymentReportsToAdd = prepaymentReports.filter(prepaymentReportItem => {
        const prepaymentReportIdentifier = getPrepaymentReportIdentifier(prepaymentReportItem);
        if (prepaymentReportIdentifier == null || prepaymentReportCollectionIdentifiers.includes(prepaymentReportIdentifier)) {
          return false;
        }
        prepaymentReportCollectionIdentifiers.push(prepaymentReportIdentifier);
        return true;
      });
      return [...prepaymentReportsToAdd, ...prepaymentReportCollection];
    }
    return prepaymentReportCollection;
  }

  protected convertDateFromClient(prepaymentReport: IPrepaymentReport): IPrepaymentReport {
    return Object.assign({}, prepaymentReport, {
      paymentDate: prepaymentReport.paymentDate?.isValid() ? prepaymentReport.paymentDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.paymentDate = res.body.paymentDate ? dayjs(res.body.paymentDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((prepaymentReport: IPrepaymentReport) => {
        prepaymentReport.paymentDate = prepaymentReport.paymentDate ? dayjs(prepaymentReport.paymentDate) : undefined;
      });
    }
    return res;
  }
}
