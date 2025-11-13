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
import { IParticularsOfOutlet, getParticularsOfOutletIdentifier } from '../particulars-of-outlet.model';

export type EntityResponseType = HttpResponse<IParticularsOfOutlet>;
export type EntityArrayResponseType = HttpResponse<IParticularsOfOutlet[]>;

@Injectable({ providedIn: 'root' })
export class ParticularsOfOutletService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/particulars-of-outlets');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/particulars-of-outlets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(particularsOfOutlet: IParticularsOfOutlet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(particularsOfOutlet);
    return this.http
      .post<IParticularsOfOutlet>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(particularsOfOutlet: IParticularsOfOutlet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(particularsOfOutlet);
    return this.http
      .put<IParticularsOfOutlet>(`${this.resourceUrl}/${getParticularsOfOutletIdentifier(particularsOfOutlet) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(particularsOfOutlet: IParticularsOfOutlet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(particularsOfOutlet);
    return this.http
      .patch<IParticularsOfOutlet>(`${this.resourceUrl}/${getParticularsOfOutletIdentifier(particularsOfOutlet) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IParticularsOfOutlet>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IParticularsOfOutlet[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IParticularsOfOutlet[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addParticularsOfOutletToCollectionIfMissing(
    particularsOfOutletCollection: IParticularsOfOutlet[],
    ...particularsOfOutletsToCheck: (IParticularsOfOutlet | null | undefined)[]
  ): IParticularsOfOutlet[] {
    const particularsOfOutlets: IParticularsOfOutlet[] = particularsOfOutletsToCheck.filter(isPresent);
    if (particularsOfOutlets.length > 0) {
      const particularsOfOutletCollectionIdentifiers = particularsOfOutletCollection.map(
        particularsOfOutletItem => getParticularsOfOutletIdentifier(particularsOfOutletItem)!
      );
      const particularsOfOutletsToAdd = particularsOfOutlets.filter(particularsOfOutletItem => {
        const particularsOfOutletIdentifier = getParticularsOfOutletIdentifier(particularsOfOutletItem);
        if (particularsOfOutletIdentifier == null || particularsOfOutletCollectionIdentifiers.includes(particularsOfOutletIdentifier)) {
          return false;
        }
        particularsOfOutletCollectionIdentifiers.push(particularsOfOutletIdentifier);
        return true;
      });
      return [...particularsOfOutletsToAdd, ...particularsOfOutletCollection];
    }
    return particularsOfOutletCollection;
  }

  protected convertDateFromClient(particularsOfOutlet: IParticularsOfOutlet): IParticularsOfOutlet {
    return Object.assign({}, particularsOfOutlet, {
      businessReportingDate: particularsOfOutlet.businessReportingDate?.isValid()
        ? particularsOfOutlet.businessReportingDate.format(DATE_FORMAT)
        : undefined,
      cbkApprovalDate: particularsOfOutlet.cbkApprovalDate?.isValid() ? particularsOfOutlet.cbkApprovalDate.format(DATE_FORMAT) : undefined,
      outletOpeningDate: particularsOfOutlet.outletOpeningDate?.isValid()
        ? particularsOfOutlet.outletOpeningDate.format(DATE_FORMAT)
        : undefined,
      outletClosureDate: particularsOfOutlet.outletClosureDate?.isValid()
        ? particularsOfOutlet.outletClosureDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.businessReportingDate = res.body.businessReportingDate ? dayjs(res.body.businessReportingDate) : undefined;
      res.body.cbkApprovalDate = res.body.cbkApprovalDate ? dayjs(res.body.cbkApprovalDate) : undefined;
      res.body.outletOpeningDate = res.body.outletOpeningDate ? dayjs(res.body.outletOpeningDate) : undefined;
      res.body.outletClosureDate = res.body.outletClosureDate ? dayjs(res.body.outletClosureDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((particularsOfOutlet: IParticularsOfOutlet) => {
        particularsOfOutlet.businessReportingDate = particularsOfOutlet.businessReportingDate
          ? dayjs(particularsOfOutlet.businessReportingDate)
          : undefined;
        particularsOfOutlet.cbkApprovalDate = particularsOfOutlet.cbkApprovalDate ? dayjs(particularsOfOutlet.cbkApprovalDate) : undefined;
        particularsOfOutlet.outletOpeningDate = particularsOfOutlet.outletOpeningDate
          ? dayjs(particularsOfOutlet.outletOpeningDate)
          : undefined;
        particularsOfOutlet.outletClosureDate = particularsOfOutlet.outletClosureDate
          ? dayjs(particularsOfOutlet.outletClosureDate)
          : undefined;
      });
    }
    return res;
  }
}
