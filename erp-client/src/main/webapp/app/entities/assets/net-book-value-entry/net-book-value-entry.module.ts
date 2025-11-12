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
import { NetBookValueEntryComponent } from './list/net-book-value-entry.component';
import { NetBookValueEntryDetailComponent } from './detail/net-book-value-entry-detail.component';
import { NetBookValueEntryUpdateComponent } from './update/net-book-value-entry-update.component';
import { NetBookValueEntryDeleteDialogComponent } from './delete/net-book-value-entry-delete-dialog.component';
import { NetBookValueEntryRoutingModule } from './route/net-book-value-entry-routing.module';

@NgModule({
  imports: [SharedModule, NetBookValueEntryRoutingModule],
  declarations: [
    NetBookValueEntryComponent,
    NetBookValueEntryDetailComponent,
    NetBookValueEntryUpdateComponent,
    NetBookValueEntryDeleteDialogComponent,
  ],
  entryComponents: [NetBookValueEntryDeleteDialogComponent],
})
export class NetBookValueEntryModule {}
