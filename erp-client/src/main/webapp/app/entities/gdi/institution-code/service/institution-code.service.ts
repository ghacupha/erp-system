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
import { IInstitutionCode, getInstitutionCodeIdentifier } from '../institution-code.model';

export type EntityResponseType = HttpResponse<IInstitutionCode>;
export type EntityArrayResponseType = HttpResponse<IInstitutionCode[]>;

@Injectable({ providedIn: 'root' })
export class InstitutionCodeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/institution-codes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/institution-codes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(institutionCode: IInstitutionCode): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(institutionCode);
    return this.http
      .post<IInstitutionCode>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(institutionCode: IInstitutionCode): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(institutionCode);
    return this.http
      .put<IInstitutionCode>(`${this.resourceUrl}/${getInstitutionCodeIdentifier(institutionCode) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(institutionCode: IInstitutionCode): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(institutionCode);
    return this.http
      .patch<IInstitutionCode>(`${this.resourceUrl}/${getInstitutionCodeIdentifier(institutionCode) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInstitutionCode>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInstitutionCode[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInstitutionCode[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addInstitutionCodeToCollectionIfMissing(
    institutionCodeCollection: IInstitutionCode[],
    ...institutionCodesToCheck: (IInstitutionCode | null | undefined)[]
  ): IInstitutionCode[] {
    const institutionCodes: IInstitutionCode[] = institutionCodesToCheck.filter(isPresent);
    if (institutionCodes.length > 0) {
      const institutionCodeCollectionIdentifiers = institutionCodeCollection.map(
        institutionCodeItem => getInstitutionCodeIdentifier(institutionCodeItem)!
      );
      const institutionCodesToAdd = institutionCodes.filter(institutionCodeItem => {
        const institutionCodeIdentifier = getInstitutionCodeIdentifier(institutionCodeItem);
        if (institutionCodeIdentifier == null || institutionCodeCollectionIdentifiers.includes(institutionCodeIdentifier)) {
          return false;
        }
        institutionCodeCollectionIdentifiers.push(institutionCodeIdentifier);
        return true;
      });
      return [...institutionCodesToAdd, ...institutionCodeCollection];
    }
    return institutionCodeCollection;
  }

  protected convertDateFromClient(institutionCode: IInstitutionCode): IInstitutionCode {
    return Object.assign({}, institutionCode, {
      dateLicensed: institutionCode.dateLicensed?.isValid() ? institutionCode.dateLicensed.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateLicensed = res.body.dateLicensed ? dayjs(res.body.dateLicensed) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((institutionCode: IInstitutionCode) => {
        institutionCode.dateLicensed = institutionCode.dateLicensed ? dayjs(institutionCode.dateLicensed) : undefined;
      });
    }
    return res;
  }
}
