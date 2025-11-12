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
import { ILeaseLiability, getLeaseLiabilityIdentifier } from '../lease-liability.model';

export type EntityResponseType = HttpResponse<ILeaseLiability>;
export type EntityArrayResponseType = HttpResponse<ILeaseLiability[]>;

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/lease-liabilities');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/lease-liabilities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaseLiability: ILeaseLiability): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiability);
    return this.http
      .post<ILeaseLiability>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leaseLiability: ILeaseLiability): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiability);
    return this.http
      .put<ILeaseLiability>(`${this.resourceUrl}/${getLeaseLiabilityIdentifier(leaseLiability) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(leaseLiability: ILeaseLiability): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiability);
    return this.http
      .patch<ILeaseLiability>(`${this.resourceUrl}/${getLeaseLiabilityIdentifier(leaseLiability) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeaseLiability>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseLiability[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseLiability[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addLeaseLiabilityToCollectionIfMissing(
    leaseLiabilityCollection: ILeaseLiability[],
    ...leaseLiabilitiesToCheck: (ILeaseLiability | null | undefined)[]
  ): ILeaseLiability[] {
    const leaseLiabilities: ILeaseLiability[] = leaseLiabilitiesToCheck.filter(isPresent);
    if (leaseLiabilities.length > 0) {
      const leaseLiabilityCollectionIdentifiers = leaseLiabilityCollection.map(
        leaseLiabilityItem => getLeaseLiabilityIdentifier(leaseLiabilityItem)!
      );
      const leaseLiabilitiesToAdd = leaseLiabilities.filter(leaseLiabilityItem => {
        const leaseLiabilityIdentifier = getLeaseLiabilityIdentifier(leaseLiabilityItem);
        if (leaseLiabilityIdentifier == null || leaseLiabilityCollectionIdentifiers.includes(leaseLiabilityIdentifier)) {
          return false;
        }
        leaseLiabilityCollectionIdentifiers.push(leaseLiabilityIdentifier);
        return true;
      });
      return [...leaseLiabilitiesToAdd, ...leaseLiabilityCollection];
    }
    return leaseLiabilityCollection;
  }

  protected convertDateFromClient(leaseLiability: ILeaseLiability): ILeaseLiability {
    return Object.assign({}, leaseLiability, {
      startDate: leaseLiability.startDate?.isValid() ? leaseLiability.startDate.format(DATE_FORMAT) : undefined,
      endDate: leaseLiability.endDate?.isValid() ? leaseLiability.endDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((leaseLiability: ILeaseLiability) => {
        leaseLiability.startDate = leaseLiability.startDate ? dayjs(leaseLiability.startDate) : undefined;
        leaseLiability.endDate = leaseLiability.endDate ? dayjs(leaseLiability.endDate) : undefined;
      });
    }
    return res;
  }
}
