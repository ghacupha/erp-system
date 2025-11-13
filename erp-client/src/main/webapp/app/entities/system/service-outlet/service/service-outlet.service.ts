///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
import { IServiceOutlet, getServiceOutletIdentifier } from '../service-outlet.model';

export type EntityResponseType = HttpResponse<IServiceOutlet>;
export type EntityArrayResponseType = HttpResponse<IServiceOutlet[]>;

@Injectable({ providedIn: 'root' })
export class ServiceOutletService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/service-outlets');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/service-outlets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(serviceOutlet: IServiceOutlet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceOutlet);
    return this.http
      .post<IServiceOutlet>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(serviceOutlet: IServiceOutlet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceOutlet);
    return this.http
      .put<IServiceOutlet>(`${this.resourceUrl}/${getServiceOutletIdentifier(serviceOutlet) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(serviceOutlet: IServiceOutlet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceOutlet);
    return this.http
      .patch<IServiceOutlet>(`${this.resourceUrl}/${getServiceOutletIdentifier(serviceOutlet) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IServiceOutlet>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IServiceOutlet[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IServiceOutlet[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addServiceOutletToCollectionIfMissing(
    serviceOutletCollection: IServiceOutlet[],
    ...serviceOutletsToCheck: (IServiceOutlet | null | undefined)[]
  ): IServiceOutlet[] {
    const serviceOutlets: IServiceOutlet[] = serviceOutletsToCheck.filter(isPresent);
    if (serviceOutlets.length > 0) {
      const serviceOutletCollectionIdentifiers = serviceOutletCollection.map(
        serviceOutletItem => getServiceOutletIdentifier(serviceOutletItem)!
      );
      const serviceOutletsToAdd = serviceOutlets.filter(serviceOutletItem => {
        const serviceOutletIdentifier = getServiceOutletIdentifier(serviceOutletItem);
        if (serviceOutletIdentifier == null || serviceOutletCollectionIdentifiers.includes(serviceOutletIdentifier)) {
          return false;
        }
        serviceOutletCollectionIdentifiers.push(serviceOutletIdentifier);
        return true;
      });
      return [...serviceOutletsToAdd, ...serviceOutletCollection];
    }
    return serviceOutletCollection;
  }

  protected convertDateFromClient(serviceOutlet: IServiceOutlet): IServiceOutlet {
    return Object.assign({}, serviceOutlet, {
      outletOpeningDate: serviceOutlet.outletOpeningDate?.isValid() ? serviceOutlet.outletOpeningDate.format(DATE_FORMAT) : undefined,
      regulatorApprovalDate: serviceOutlet.regulatorApprovalDate?.isValid()
        ? serviceOutlet.regulatorApprovalDate.format(DATE_FORMAT)
        : undefined,
      outletClosureDate: serviceOutlet.outletClosureDate?.isValid() ? serviceOutlet.outletClosureDate.format(DATE_FORMAT) : undefined,
      dateLastModified: serviceOutlet.dateLastModified?.isValid() ? serviceOutlet.dateLastModified.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.outletOpeningDate = res.body.outletOpeningDate ? dayjs(res.body.outletOpeningDate) : undefined;
      res.body.regulatorApprovalDate = res.body.regulatorApprovalDate ? dayjs(res.body.regulatorApprovalDate) : undefined;
      res.body.outletClosureDate = res.body.outletClosureDate ? dayjs(res.body.outletClosureDate) : undefined;
      res.body.dateLastModified = res.body.dateLastModified ? dayjs(res.body.dateLastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((serviceOutlet: IServiceOutlet) => {
        serviceOutlet.outletOpeningDate = serviceOutlet.outletOpeningDate ? dayjs(serviceOutlet.outletOpeningDate) : undefined;
        serviceOutlet.regulatorApprovalDate = serviceOutlet.regulatorApprovalDate ? dayjs(serviceOutlet.regulatorApprovalDate) : undefined;
        serviceOutlet.outletClosureDate = serviceOutlet.outletClosureDate ? dayjs(serviceOutlet.outletClosureDate) : undefined;
        serviceOutlet.dateLastModified = serviceOutlet.dateLastModified ? dayjs(serviceOutlet.dateLastModified) : undefined;
      });
    }
    return res;
  }
}
