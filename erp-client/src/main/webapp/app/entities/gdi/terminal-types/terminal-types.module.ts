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

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TerminalTypesComponent } from './list/terminal-types.component';
import { TerminalTypesDetailComponent } from './detail/terminal-types-detail.component';
import { TerminalTypesUpdateComponent } from './update/terminal-types-update.component';
import { TerminalTypesDeleteDialogComponent } from './delete/terminal-types-delete-dialog.component';
import { TerminalTypesRoutingModule } from './route/terminal-types-routing.module';

@NgModule({
  imports: [SharedModule, TerminalTypesRoutingModule],
  declarations: [TerminalTypesComponent, TerminalTypesDetailComponent, TerminalTypesUpdateComponent, TerminalTypesDeleteDialogComponent],
  entryComponents: [TerminalTypesDeleteDialogComponent],
})
export class TerminalTypesModule {}
