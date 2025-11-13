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

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NbvCompilationJobComponent } from './list/nbv-compilation-job.component';
import { NbvCompilationJobDetailComponent } from './detail/nbv-compilation-job-detail.component';
import { NbvCompilationJobUpdateComponent } from './update/nbv-compilation-job-update.component';
import { NbvCompilationJobDeleteDialogComponent } from './delete/nbv-compilation-job-delete-dialog.component';
import { NbvCompilationJobRoutingModule } from './route/nbv-compilation-job-routing.module';

@NgModule({
  imports: [SharedModule, NbvCompilationJobRoutingModule],
  declarations: [
    NbvCompilationJobComponent,
    NbvCompilationJobDetailComponent,
    NbvCompilationJobUpdateComponent,
    NbvCompilationJobDeleteDialogComponent,
  ],
  entryComponents: [NbvCompilationJobDeleteDialogComponent],
})
export class NbvCompilationJobModule {}
