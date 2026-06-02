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
import { IXlsxReportRequisition, getXlsxReportRequisitionIdentifier } from '../xlsx-report-requisition.model';

export type EntityResponseType = HttpResponse<IXlsxReportRequisition>;
export type EntityArrayResponseType = HttpResponse<IXlsxReportRequisition[]>;

@Injectable({ providedIn: 'root' })
export class XlsxReportRequisitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/xlsx-report-requisitions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/xlsx-report-requisitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(xlsxReportRequisition: IXlsxReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(xlsxReportRequisition);
    return this.http
      .post<IXlsxReportRequisition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(xlsxReportRequisition: IXlsxReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(xlsxReportRequisition);
    return this.http
      .put<IXlsxReportRequisition>(`${this.resourceUrl}/${getXlsxReportRequisitionIdentifier(xlsxReportRequisition) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(xlsxReportRequisition: IXlsxReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(xlsxReportRequisition);
    return this.http
      .patch<IXlsxReportRequisition>(`${this.resourceUrl}/${getXlsxReportRequisitionIdentifier(xlsxReportRequisition) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IXlsxReportRequisition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IXlsxReportRequisition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IXlsxReportRequisition[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addXlsxReportRequisitionToCollectionIfMissing(
    xlsxReportRequisitionCollection: IXlsxReportRequisition[],
    ...xlsxReportRequisitionsToCheck: (IXlsxReportRequisition | null | undefined)[]
  ): IXlsxReportRequisition[] {
    const xlsxReportRequisitions: IXlsxReportRequisition[] = xlsxReportRequisitionsToCheck.filter(isPresent);
    if (xlsxReportRequisitions.length > 0) {
      const xlsxReportRequisitionCollectionIdentifiers = xlsxReportRequisitionCollection.map(
        xlsxReportRequisitionItem => getXlsxReportRequisitionIdentifier(xlsxReportRequisitionItem)!
      );
      const xlsxReportRequisitionsToAdd = xlsxReportRequisitions.filter(xlsxReportRequisitionItem => {
        const xlsxReportRequisitionIdentifier = getXlsxReportRequisitionIdentifier(xlsxReportRequisitionItem);
        if (
          xlsxReportRequisitionIdentifier == null ||
          xlsxReportRequisitionCollectionIdentifiers.includes(xlsxReportRequisitionIdentifier)
        ) {
          return false;
        }
        xlsxReportRequisitionCollectionIdentifiers.push(xlsxReportRequisitionIdentifier);
        return true;
      });
      return [...xlsxReportRequisitionsToAdd, ...xlsxReportRequisitionCollection];
    }
    return xlsxReportRequisitionCollection;
  }

  protected convertDateFromClient(xlsxReportRequisition: IXlsxReportRequisition): IXlsxReportRequisition {
    return Object.assign({}, xlsxReportRequisition, {
      reportDate: xlsxReportRequisition.reportDate?.isValid() ? xlsxReportRequisition.reportDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.reportDate = res.body.reportDate ? dayjs(res.body.reportDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((xlsxReportRequisition: IXlsxReportRequisition) => {
        xlsxReportRequisition.reportDate = xlsxReportRequisition.reportDate ? dayjs(xlsxReportRequisition.reportDate) : undefined;
      });
    }
    return res;
  }
}
