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
import { TARecognitionROURuleComponent } from './list/ta-recognition-rou-rule.component';
import { TARecognitionROURuleDetailComponent } from './detail/ta-recognition-rou-rule-detail.component';
import { TARecognitionROURuleUpdateComponent } from './update/ta-recognition-rou-rule-update.component';
import { TARecognitionROURuleDeleteDialogComponent } from './delete/ta-recognition-rou-rule-delete-dialog.component';
import { TARecognitionROURuleRoutingModule } from './route/ta-recognition-rou-rule-routing.module';

@NgModule({
  imports: [SharedModule, TARecognitionROURuleRoutingModule],
  declarations: [
    TARecognitionROURuleComponent,
    TARecognitionROURuleDetailComponent,
    TARecognitionROURuleUpdateComponent,
    TARecognitionROURuleDeleteDialogComponent,
  ],
  entryComponents: [TARecognitionROURuleDeleteDialogComponent],
})
export class TARecognitionROURuleModule {}
