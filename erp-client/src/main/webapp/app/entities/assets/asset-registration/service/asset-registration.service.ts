///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
import { IAssetRegistration, getAssetRegistrationIdentifier } from '../asset-registration.model';

export type EntityResponseType = HttpResponse<IAssetRegistration>;
export type EntityArrayResponseType = HttpResponse<IAssetRegistration[]>;

@Injectable({ providedIn: 'root' })
export class AssetRegistrationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-registrations');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/asset-registrations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assetRegistration: IAssetRegistration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetRegistration);
    return this.http
      .post<IAssetRegistration>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(assetRegistration: IAssetRegistration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetRegistration);
    return this.http
      .put<IAssetRegistration>(`${this.resourceUrl}/${getAssetRegistrationIdentifier(assetRegistration) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(assetRegistration: IAssetRegistration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetRegistration);
    return this.http
      .patch<IAssetRegistration>(`${this.resourceUrl}/${getAssetRegistrationIdentifier(assetRegistration) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAssetRegistration>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetRegistration[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetRegistration[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAssetRegistrationToCollectionIfMissing(
    assetRegistrationCollection: IAssetRegistration[],
    ...assetRegistrationsToCheck: (IAssetRegistration | null | undefined)[]
  ): IAssetRegistration[] {
    const assetRegistrations: IAssetRegistration[] = assetRegistrationsToCheck.filter(isPresent);
    if (assetRegistrations.length > 0) {
      const assetRegistrationCollectionIdentifiers = assetRegistrationCollection.map(
        assetRegistrationItem => getAssetRegistrationIdentifier(assetRegistrationItem)!
      );
      const assetRegistrationsToAdd = assetRegistrations.filter(assetRegistrationItem => {
        const assetRegistrationIdentifier = getAssetRegistrationIdentifier(assetRegistrationItem);
        if (assetRegistrationIdentifier == null || assetRegistrationCollectionIdentifiers.includes(assetRegistrationIdentifier)) {
          return false;
        }
        assetRegistrationCollectionIdentifiers.push(assetRegistrationIdentifier);
        return true;
      });
      return [...assetRegistrationsToAdd, ...assetRegistrationCollection];
    }
    return assetRegistrationCollection;
  }

  protected convertDateFromClient(assetRegistration: IAssetRegistration): IAssetRegistration {
    return Object.assign({}, assetRegistration, {
      capitalizationDate: assetRegistration.capitalizationDate?.isValid()
        ? assetRegistration.capitalizationDate.format(DATE_FORMAT)
        : undefined,
      registrationDate: assetRegistration.registrationDate?.isValid() ? assetRegistration.registrationDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.capitalizationDate = res.body.capitalizationDate ? dayjs(res.body.capitalizationDate) : undefined;
      res.body.registrationDate = res.body.registrationDate ? dayjs(res.body.registrationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((assetRegistration: IAssetRegistration) => {
        assetRegistration.capitalizationDate = assetRegistration.capitalizationDate
          ? dayjs(assetRegistration.capitalizationDate)
          : undefined;
        assetRegistration.registrationDate = assetRegistration.registrationDate ? dayjs(assetRegistration.registrationDate) : undefined;
      });
    }
    return res;
  }
}
