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

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITACompilationRequest } from '../ta-compilation-request.model';
import { TACompilationRequestService } from '../service/ta-compilation-request.service';

@Component({
  templateUrl: './ta-compilation-request-delete-dialog.component.html',
})
export class TACompilationRequestDeleteDialogComponent {
  tACompilationRequest?: ITACompilationRequest;

  constructor(protected tACompilationRequestService: TACompilationRequestService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tACompilationRequestService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
