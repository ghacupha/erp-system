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
import { ICollateralInformation, getCollateralInformationIdentifier } from '../collateral-information.model';

export type EntityResponseType = HttpResponse<ICollateralInformation>;
export type EntityArrayResponseType = HttpResponse<ICollateralInformation[]>;

@Injectable({ providedIn: 'root' })
export class CollateralInformationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/collateral-informations');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/collateral-informations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(collateralInformation: ICollateralInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(collateralInformation);
    return this.http
      .post<ICollateralInformation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(collateralInformation: ICollateralInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(collateralInformation);
    return this.http
      .put<ICollateralInformation>(`${this.resourceUrl}/${getCollateralInformationIdentifier(collateralInformation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(collateralInformation: ICollateralInformation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(collateralInformation);
    return this.http
      .patch<ICollateralInformation>(`${this.resourceUrl}/${getCollateralInformationIdentifier(collateralInformation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICollateralInformation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICollateralInformation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICollateralInformation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addCollateralInformationToCollectionIfMissing(
    collateralInformationCollection: ICollateralInformation[],
    ...collateralInformationsToCheck: (ICollateralInformation | null | undefined)[]
  ): ICollateralInformation[] {
    const collateralInformations: ICollateralInformation[] = collateralInformationsToCheck.filter(isPresent);
    if (collateralInformations.length > 0) {
      const collateralInformationCollectionIdentifiers = collateralInformationCollection.map(
        collateralInformationItem => getCollateralInformationIdentifier(collateralInformationItem)!
      );
      const collateralInformationsToAdd = collateralInformations.filter(collateralInformationItem => {
        const collateralInformationIdentifier = getCollateralInformationIdentifier(collateralInformationItem);
        if (
          collateralInformationIdentifier == null ||
          collateralInformationCollectionIdentifiers.includes(collateralInformationIdentifier)
        ) {
          return false;
        }
        collateralInformationCollectionIdentifiers.push(collateralInformationIdentifier);
        return true;
      });
      return [...collateralInformationsToAdd, ...collateralInformationCollection];
    }
    return collateralInformationCollection;
  }

  protected convertDateFromClient(collateralInformation: ICollateralInformation): ICollateralInformation {
    return Object.assign({}, collateralInformation, {
      reportingDate: collateralInformation.reportingDate?.isValid() ? collateralInformation.reportingDate.format(DATE_FORMAT) : undefined,
      collateralLastValuationDate: collateralInformation.collateralLastValuationDate?.isValid()
        ? collateralInformation.collateralLastValuationDate.format(DATE_FORMAT)
        : undefined,
      insuranceExpiryDate: collateralInformation.insuranceExpiryDate?.isValid()
        ? collateralInformation.insuranceExpiryDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.reportingDate = res.body.reportingDate ? dayjs(res.body.reportingDate) : undefined;
      res.body.collateralLastValuationDate = res.body.collateralLastValuationDate ? dayjs(res.body.collateralLastValuationDate) : undefined;
      res.body.insuranceExpiryDate = res.body.insuranceExpiryDate ? dayjs(res.body.insuranceExpiryDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((collateralInformation: ICollateralInformation) => {
        collateralInformation.reportingDate = collateralInformation.reportingDate ? dayjs(collateralInformation.reportingDate) : undefined;
        collateralInformation.collateralLastValuationDate = collateralInformation.collateralLastValuationDate
          ? dayjs(collateralInformation.collateralLastValuationDate)
          : undefined;
        collateralInformation.insuranceExpiryDate = collateralInformation.insuranceExpiryDate
          ? dayjs(collateralInformation.insuranceExpiryDate)
          : undefined;
      });
    }
    return res;
  }
}
