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

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILeaseAmortizationCalculation, getLeaseAmortizationCalculationIdentifier } from '../lease-amortization-calculation.model';

export type EntityResponseType = HttpResponse<ILeaseAmortizationCalculation>;
export type EntityArrayResponseType = HttpResponse<ILeaseAmortizationCalculation[]>;

@Injectable({ providedIn: 'root' })
export class LeaseAmortizationCalculationService {
  protected resourceUrlByLeaseContractId = this.applicationConfigService.getEndpointFor('api/leases/lease-amortization-calculations/by-lease-contract-id');
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/lease-amortization-calculations');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/lease-amortization-calculations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaseAmortizationCalculation: ILeaseAmortizationCalculation): Observable<EntityResponseType> {
    return this.http.post<ILeaseAmortizationCalculation>(this.resourceUrl, leaseAmortizationCalculation, { observe: 'response' });
  }

  update(leaseAmortizationCalculation: ILeaseAmortizationCalculation): Observable<EntityResponseType> {
    return this.http.put<ILeaseAmortizationCalculation>(
      `${this.resourceUrl}/${getLeaseAmortizationCalculationIdentifier(leaseAmortizationCalculation) as number}`,
      leaseAmortizationCalculation,
      { observe: 'response' }
    );
  }

  partialUpdate(leaseAmortizationCalculation: ILeaseAmortizationCalculation): Observable<EntityResponseType> {
    return this.http.patch<ILeaseAmortizationCalculation>(
      `${this.resourceUrl}/${getLeaseAmortizationCalculationIdentifier(leaseAmortizationCalculation) as number}`,
      leaseAmortizationCalculation,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeaseAmortizationCalculation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseAmortizationCalculation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryByLeaseContractId(leaseContractId: number): Observable<EntityResponseType> {
    return this.http.get<ILeaseAmortizationCalculation>(`${this.resourceUrlByLeaseContractId}/${leaseContractId}`, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseAmortizationCalculation[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLeaseAmortizationCalculationToCollectionIfMissing(
    leaseAmortizationCalculationCollection: ILeaseAmortizationCalculation[],
    ...leaseAmortizationCalculationsToCheck: (ILeaseAmortizationCalculation | null | undefined)[]
  ): ILeaseAmortizationCalculation[] {
    const leaseAmortizationCalculations: ILeaseAmortizationCalculation[] = leaseAmortizationCalculationsToCheck.filter(isPresent);
    if (leaseAmortizationCalculations.length > 0) {
      const leaseAmortizationCalculationCollectionIdentifiers = leaseAmortizationCalculationCollection.map(
        leaseAmortizationCalculationItem => getLeaseAmortizationCalculationIdentifier(leaseAmortizationCalculationItem)!
      );
      const leaseAmortizationCalculationsToAdd = leaseAmortizationCalculations.filter(leaseAmortizationCalculationItem => {
        const leaseAmortizationCalculationIdentifier = getLeaseAmortizationCalculationIdentifier(leaseAmortizationCalculationItem);
        if (
          leaseAmortizationCalculationIdentifier == null ||
          leaseAmortizationCalculationCollectionIdentifiers.includes(leaseAmortizationCalculationIdentifier)
        ) {
          return false;
        }
        leaseAmortizationCalculationCollectionIdentifiers.push(leaseAmortizationCalculationIdentifier);
        return true;
      });
      return [...leaseAmortizationCalculationsToAdd, ...leaseAmortizationCalculationCollection];
    }
    return leaseAmortizationCalculationCollection;
  }
}
