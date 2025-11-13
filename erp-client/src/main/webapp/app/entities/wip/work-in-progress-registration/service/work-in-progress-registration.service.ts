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
import { IWorkInProgressRegistration, getWorkInProgressRegistrationIdentifier } from '../work-in-progress-registration.model';

export type EntityResponseType = HttpResponse<IWorkInProgressRegistration>;
export type EntityArrayResponseType = HttpResponse<IWorkInProgressRegistration[]>;

@Injectable({ providedIn: 'root' })
export class WorkInProgressRegistrationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/work-in-progress-registrations');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/work-in-progress-registrations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(workInProgressRegistration: IWorkInProgressRegistration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workInProgressRegistration);
    return this.http
      .post<IWorkInProgressRegistration>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(workInProgressRegistration: IWorkInProgressRegistration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workInProgressRegistration);
    return this.http
      .put<IWorkInProgressRegistration>(
        `${this.resourceUrl}/${getWorkInProgressRegistrationIdentifier(workInProgressRegistration) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(workInProgressRegistration: IWorkInProgressRegistration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workInProgressRegistration);
    return this.http
      .patch<IWorkInProgressRegistration>(
        `${this.resourceUrl}/${getWorkInProgressRegistrationIdentifier(workInProgressRegistration) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWorkInProgressRegistration>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorkInProgressRegistration[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorkInProgressRegistration[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addWorkInProgressRegistrationToCollectionIfMissing(
    workInProgressRegistrationCollection: IWorkInProgressRegistration[],
    ...workInProgressRegistrationsToCheck: (IWorkInProgressRegistration | null | undefined)[]
  ): IWorkInProgressRegistration[] {
    const workInProgressRegistrations: IWorkInProgressRegistration[] = workInProgressRegistrationsToCheck.filter(isPresent);
    if (workInProgressRegistrations.length > 0) {
      const workInProgressRegistrationCollectionIdentifiers = workInProgressRegistrationCollection.map(
        workInProgressRegistrationItem => getWorkInProgressRegistrationIdentifier(workInProgressRegistrationItem)!
      );
      const workInProgressRegistrationsToAdd = workInProgressRegistrations.filter(workInProgressRegistrationItem => {
        const workInProgressRegistrationIdentifier = getWorkInProgressRegistrationIdentifier(workInProgressRegistrationItem);
        if (
          workInProgressRegistrationIdentifier == null ||
          workInProgressRegistrationCollectionIdentifiers.includes(workInProgressRegistrationIdentifier)
        ) {
          return false;
        }
        workInProgressRegistrationCollectionIdentifiers.push(workInProgressRegistrationIdentifier);
        return true;
      });
      return [...workInProgressRegistrationsToAdd, ...workInProgressRegistrationCollection];
    }
    return workInProgressRegistrationCollection;
  }

  protected convertDateFromClient(workInProgressRegistration: IWorkInProgressRegistration): IWorkInProgressRegistration {
    return Object.assign({}, workInProgressRegistration, {
      instalmentDate: workInProgressRegistration.instalmentDate?.isValid()
        ? workInProgressRegistration.instalmentDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.instalmentDate = res.body.instalmentDate ? dayjs(res.body.instalmentDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((workInProgressRegistration: IWorkInProgressRegistration) => {
        workInProgressRegistration.instalmentDate = workInProgressRegistration.instalmentDate
          ? dayjs(workInProgressRegistration.instalmentDate)
          : undefined;
      });
    }
    return res;
  }
}
