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
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CsvFileUploadService } from '../../../erp-files/csv-file-upload/service/csv-file-upload.service';
import { ICsvFileUpload } from '../../../erp-files/csv-file-upload/csv-file-upload.model';

@Injectable({ providedIn: 'root' })
export class CsvFileUploadSuggestionService {
  constructor(private csvFileUploadService: CsvFileUploadService) {}

  search(searchText: string): Observable<ICsvFileUpload[]> {
    if (!searchText) {
      return of([]);
    }

    return this.csvFileUploadService
      .search({ query: searchText, page: 0, size: 10 })
      .pipe(map(response => response.body ?? []));
  }
}
