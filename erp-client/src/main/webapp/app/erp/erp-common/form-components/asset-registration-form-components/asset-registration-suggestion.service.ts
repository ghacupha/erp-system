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
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../../../../core/config/application-config.service';
import { from, Observable, of } from 'rxjs';
import { createRequestOption } from '../../../../core/request/request-util';
import { ASC, DESC } from '../../../../config/pagination.constants';
import { IAssetRegistration } from '../../../erp-assets/asset-registration/asset-registration.model';
import { AssetRegistrationService } from '../../../erp-assets/asset-registration/service/asset-registration.service';
import { catchError, map, mergeMap, switchMap, toArray } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AssetRegistrationSuggestionService {

  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/_search/asset-registrations');

  constructor(
    protected http: HttpClient,
    protected valueService: AssetRegistrationService,
    protected applicationConfigService: ApplicationConfigService
  ) {}

  search(searchText: string): Observable<IAssetRegistration[]> {
    if (searchText === '') {
      return of([]);
    }

    return this.http.get<IAssetRegistration[]>(this.resourceSearchUrl, {
      params: createRequestOption({
        query: searchText,
        page: 0,
        size: 10,
        sort: this.sort(),
      })
    }).pipe(
      switchMap(searchResults => from(searchResults).pipe(
        mergeMap(result =>
          this.valueService.find(<number>result.id).pipe(
            map(response => response.body as IAssetRegistration),
            catchError(() => of(null)) // Return null if the item does not exist in the backend
          )
        ),
        toArray(), // Collect the results back into an array
        map(results => results.filter(result => result !== null) as IAssetRegistration[]) // Filter out null results
      ))
    );
  }

  sort(): string[] {
    const predicate = 'id';
    const ascending = true;

    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    const result = [predicate + ',' + (ascending ? ASC : DESC)];
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    if (predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

}
