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
import { IAmortizationPeriod, getAmortizationPeriodIdentifier } from '../amortization-period.model';

export type EntityResponseType = HttpResponse<IAmortizationPeriod>;
export type EntityArrayResponseType = HttpResponse<IAmortizationPeriod[]>;

@Injectable({ providedIn: 'root' })
export class AmortizationPeriodService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/amortization-periods');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/amortization-periods');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(amortizationPeriod: IAmortizationPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(amortizationPeriod);
    return this.http
      .post<IAmortizationPeriod>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(amortizationPeriod: IAmortizationPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(amortizationPeriod);
    return this.http
      .put<IAmortizationPeriod>(`${this.resourceUrl}/${getAmortizationPeriodIdentifier(amortizationPeriod) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(amortizationPeriod: IAmortizationPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(amortizationPeriod);
    return this.http
      .patch<IAmortizationPeriod>(`${this.resourceUrl}/${getAmortizationPeriodIdentifier(amortizationPeriod) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAmortizationPeriod>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAmortizationPeriod[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAmortizationPeriod[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAmortizationPeriodToCollectionIfMissing(
    amortizationPeriodCollection: IAmortizationPeriod[],
    ...amortizationPeriodsToCheck: (IAmortizationPeriod | null | undefined)[]
  ): IAmortizationPeriod[] {
    const amortizationPeriods: IAmortizationPeriod[] = amortizationPeriodsToCheck.filter(isPresent);
    if (amortizationPeriods.length > 0) {
      const amortizationPeriodCollectionIdentifiers = amortizationPeriodCollection.map(
        amortizationPeriodItem => getAmortizationPeriodIdentifier(amortizationPeriodItem)!
      );
      const amortizationPeriodsToAdd = amortizationPeriods.filter(amortizationPeriodItem => {
        const amortizationPeriodIdentifier = getAmortizationPeriodIdentifier(amortizationPeriodItem);
        if (amortizationPeriodIdentifier == null || amortizationPeriodCollectionIdentifiers.includes(amortizationPeriodIdentifier)) {
          return false;
        }
        amortizationPeriodCollectionIdentifiers.push(amortizationPeriodIdentifier);
        return true;
      });
      return [...amortizationPeriodsToAdd, ...amortizationPeriodCollection];
    }
    return amortizationPeriodCollection;
  }

  protected convertDateFromClient(amortizationPeriod: IAmortizationPeriod): IAmortizationPeriod {
    return Object.assign({}, amortizationPeriod, {
      startDate: amortizationPeriod.startDate?.isValid() ? amortizationPeriod.startDate.format(DATE_FORMAT) : undefined,
      endDate: amortizationPeriod.endDate?.isValid() ? amortizationPeriod.endDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((amortizationPeriod: IAmortizationPeriod) => {
        amortizationPeriod.startDate = amortizationPeriod.startDate ? dayjs(amortizationPeriod.startDate) : undefined;
        amortizationPeriod.endDate = amortizationPeriod.endDate ? dayjs(amortizationPeriod.endDate) : undefined;
      });
    }
    return res;
  }
}
