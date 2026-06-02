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
import { ILeasePeriod, getLeasePeriodIdentifier } from '../lease-period.model';

export type EntityResponseType = HttpResponse<ILeasePeriod>;
export type EntityArrayResponseType = HttpResponse<ILeasePeriod[]>;

@Injectable({ providedIn: 'root' })
export class LeasePeriodService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lease-periods');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/lease-periods');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leasePeriod: ILeasePeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leasePeriod);
    return this.http
      .post<ILeasePeriod>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leasePeriod: ILeasePeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leasePeriod);
    return this.http
      .put<ILeasePeriod>(`${this.resourceUrl}/${getLeasePeriodIdentifier(leasePeriod) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(leasePeriod: ILeasePeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leasePeriod);
    return this.http
      .patch<ILeasePeriod>(`${this.resourceUrl}/${getLeasePeriodIdentifier(leasePeriod) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeasePeriod>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeasePeriod[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeasePeriod[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addLeasePeriodToCollectionIfMissing(
    leasePeriodCollection: ILeasePeriod[],
    ...leasePeriodsToCheck: (ILeasePeriod | null | undefined)[]
  ): ILeasePeriod[] {
    const leasePeriods: ILeasePeriod[] = leasePeriodsToCheck.filter(isPresent);
    if (leasePeriods.length > 0) {
      const leasePeriodCollectionIdentifiers = leasePeriodCollection.map(leasePeriodItem => getLeasePeriodIdentifier(leasePeriodItem)!);
      const leasePeriodsToAdd = leasePeriods.filter(leasePeriodItem => {
        const leasePeriodIdentifier = getLeasePeriodIdentifier(leasePeriodItem);
        if (leasePeriodIdentifier == null || leasePeriodCollectionIdentifiers.includes(leasePeriodIdentifier)) {
          return false;
        }
        leasePeriodCollectionIdentifiers.push(leasePeriodIdentifier);
        return true;
      });
      return [...leasePeriodsToAdd, ...leasePeriodCollection];
    }
    return leasePeriodCollection;
  }

  protected convertDateFromClient(leasePeriod: ILeasePeriod): ILeasePeriod {
    return Object.assign({}, leasePeriod, {
      startDate: leasePeriod.startDate?.isValid() ? leasePeriod.startDate.format(DATE_FORMAT) : undefined,
      endDate: leasePeriod.endDate?.isValid() ? leasePeriod.endDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((leasePeriod: ILeasePeriod) => {
        leasePeriod.startDate = leasePeriod.startDate ? dayjs(leasePeriod.startDate) : undefined;
        leasePeriod.endDate = leasePeriod.endDate ? dayjs(leasePeriod.endDate) : undefined;
      });
    }
    return res;
  }
}
