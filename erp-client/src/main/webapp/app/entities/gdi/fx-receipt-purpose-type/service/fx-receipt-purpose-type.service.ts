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

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IFxReceiptPurposeType, getFxReceiptPurposeTypeIdentifier } from '../fx-receipt-purpose-type.model';

export type EntityResponseType = HttpResponse<IFxReceiptPurposeType>;
export type EntityArrayResponseType = HttpResponse<IFxReceiptPurposeType[]>;

@Injectable({ providedIn: 'root' })
export class FxReceiptPurposeTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fx-receipt-purpose-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/fx-receipt-purpose-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fxReceiptPurposeType: IFxReceiptPurposeType): Observable<EntityResponseType> {
    return this.http.post<IFxReceiptPurposeType>(this.resourceUrl, fxReceiptPurposeType, { observe: 'response' });
  }

  update(fxReceiptPurposeType: IFxReceiptPurposeType): Observable<EntityResponseType> {
    return this.http.put<IFxReceiptPurposeType>(
      `${this.resourceUrl}/${getFxReceiptPurposeTypeIdentifier(fxReceiptPurposeType) as number}`,
      fxReceiptPurposeType,
      { observe: 'response' }
    );
  }

  partialUpdate(fxReceiptPurposeType: IFxReceiptPurposeType): Observable<EntityResponseType> {
    return this.http.patch<IFxReceiptPurposeType>(
      `${this.resourceUrl}/${getFxReceiptPurposeTypeIdentifier(fxReceiptPurposeType) as number}`,
      fxReceiptPurposeType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFxReceiptPurposeType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFxReceiptPurposeType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFxReceiptPurposeType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addFxReceiptPurposeTypeToCollectionIfMissing(
    fxReceiptPurposeTypeCollection: IFxReceiptPurposeType[],
    ...fxReceiptPurposeTypesToCheck: (IFxReceiptPurposeType | null | undefined)[]
  ): IFxReceiptPurposeType[] {
    const fxReceiptPurposeTypes: IFxReceiptPurposeType[] = fxReceiptPurposeTypesToCheck.filter(isPresent);
    if (fxReceiptPurposeTypes.length > 0) {
      const fxReceiptPurposeTypeCollectionIdentifiers = fxReceiptPurposeTypeCollection.map(
        fxReceiptPurposeTypeItem => getFxReceiptPurposeTypeIdentifier(fxReceiptPurposeTypeItem)!
      );
      const fxReceiptPurposeTypesToAdd = fxReceiptPurposeTypes.filter(fxReceiptPurposeTypeItem => {
        const fxReceiptPurposeTypeIdentifier = getFxReceiptPurposeTypeIdentifier(fxReceiptPurposeTypeItem);
        if (fxReceiptPurposeTypeIdentifier == null || fxReceiptPurposeTypeCollectionIdentifiers.includes(fxReceiptPurposeTypeIdentifier)) {
          return false;
        }
        fxReceiptPurposeTypeCollectionIdentifiers.push(fxReceiptPurposeTypeIdentifier);
        return true;
      });
      return [...fxReceiptPurposeTypesToAdd, ...fxReceiptPurposeTypeCollection];
    }
    return fxReceiptPurposeTypeCollection;
  }
}
