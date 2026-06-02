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
  IAmortizationPostingReportRequisition,
  getAmortizationPostingReportRequisitionIdentifier,
} from '../amortization-posting-report-requisition.model';

export type EntityResponseType = HttpResponse<IAmortizationPostingReportRequisition>;
export type EntityArrayResponseType = HttpResponse<IAmortizationPostingReportRequisition[]>;

@Injectable({ providedIn: 'root' })
export class AmortizationPostingReportRequisitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/amortization-posting-report-requisitions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/amortization-posting-report-requisitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(amortizationPostingReportRequisition: IAmortizationPostingReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(amortizationPostingReportRequisition);
    return this.http
      .post<IAmortizationPostingReportRequisition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(amortizationPostingReportRequisition: IAmortizationPostingReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(amortizationPostingReportRequisition);
    return this.http
      .put<IAmortizationPostingReportRequisition>(
        `${this.resourceUrl}/${getAmortizationPostingReportRequisitionIdentifier(amortizationPostingReportRequisition) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(amortizationPostingReportRequisition: IAmortizationPostingReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(amortizationPostingReportRequisition);
    return this.http
      .patch<IAmortizationPostingReportRequisition>(
        `${this.resourceUrl}/${getAmortizationPostingReportRequisitionIdentifier(amortizationPostingReportRequisition) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAmortizationPostingReportRequisition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAmortizationPostingReportRequisition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAmortizationPostingReportRequisition[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAmortizationPostingReportRequisitionToCollectionIfMissing(
    amortizationPostingReportRequisitionCollection: IAmortizationPostingReportRequisition[],
    ...amortizationPostingReportRequisitionsToCheck: (IAmortizationPostingReportRequisition | null | undefined)[]
  ): IAmortizationPostingReportRequisition[] {
    const amortizationPostingReportRequisitions: IAmortizationPostingReportRequisition[] =
      amortizationPostingReportRequisitionsToCheck.filter(isPresent);
    if (amortizationPostingReportRequisitions.length > 0) {
      const amortizationPostingReportRequisitionCollectionIdentifiers = amortizationPostingReportRequisitionCollection.map(
        amortizationPostingReportRequisitionItem =>
          getAmortizationPostingReportRequisitionIdentifier(amortizationPostingReportRequisitionItem)!
      );
      const amortizationPostingReportRequisitionsToAdd = amortizationPostingReportRequisitions.filter(
        amortizationPostingReportRequisitionItem => {
          const amortizationPostingReportRequisitionIdentifier = getAmortizationPostingReportRequisitionIdentifier(
            amortizationPostingReportRequisitionItem
          );
          if (
            amortizationPostingReportRequisitionIdentifier == null ||
            amortizationPostingReportRequisitionCollectionIdentifiers.includes(amortizationPostingReportRequisitionIdentifier)
          ) {
            return false;
          }
          amortizationPostingReportRequisitionCollectionIdentifiers.push(amortizationPostingReportRequisitionIdentifier);
          return true;
        }
      );
      return [...amortizationPostingReportRequisitionsToAdd, ...amortizationPostingReportRequisitionCollection];
    }
    return amortizationPostingReportRequisitionCollection;
  }

  protected convertDateFromClient(
    amortizationPostingReportRequisition: IAmortizationPostingReportRequisition
  ): IAmortizationPostingReportRequisition {
    return Object.assign({}, amortizationPostingReportRequisition, {
      timeOfRequisition: amortizationPostingReportRequisition.timeOfRequisition?.isValid()
        ? amortizationPostingReportRequisition.timeOfRequisition.toJSON()
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
      res.body.forEach((amortizationPostingReportRequisition: IAmortizationPostingReportRequisition) => {
        amortizationPostingReportRequisition.timeOfRequisition = amortizationPostingReportRequisition.timeOfRequisition
          ? dayjs(amortizationPostingReportRequisition.timeOfRequisition)
          : undefined;
      });
    }
    return res;
  }
}
