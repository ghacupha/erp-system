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

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbReportViewBandComponent } from './list/crb-report-view-band.component';
import { CrbReportViewBandDetailComponent } from './detail/crb-report-view-band-detail.component';
import { CrbReportViewBandUpdateComponent } from './update/crb-report-view-band-update.component';
import { CrbReportViewBandDeleteDialogComponent } from './delete/crb-report-view-band-delete-dialog.component';
import { CrbReportViewBandRoutingModule } from './route/crb-report-view-band-routing.module';

@NgModule({
  imports: [SharedModule, CrbReportViewBandRoutingModule],
  declarations: [
    CrbReportViewBandComponent,
    CrbReportViewBandDetailComponent,
    CrbReportViewBandUpdateComponent,
    CrbReportViewBandDeleteDialogComponent,
  ],
  entryComponents: [CrbReportViewBandDeleteDialogComponent],
})
export class CrbReportViewBandModule {}
