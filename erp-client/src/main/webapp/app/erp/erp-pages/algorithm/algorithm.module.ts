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
import { AlgorithmComponent } from './list/algorithm.component';
import { AlgorithmDetailComponent } from './detail/algorithm-detail.component';
import { AlgorithmUpdateComponent } from './update/algorithm-update.component';
import { AlgorithmDeleteDialogComponent } from './delete/algorithm-delete-dialog.component';
import { AlgorithmRoutingModule } from './route/algorithm-routing.module';
import { ErpCommonModule } from '../../erp-common/erp-common.module';

@NgModule({
  imports: [SharedModule, AlgorithmRoutingModule, ErpCommonModule],
  declarations: [AlgorithmComponent, AlgorithmDetailComponent, AlgorithmUpdateComponent, AlgorithmDeleteDialogComponent],
  entryComponents: [AlgorithmDeleteDialogComponent],
})
export class AlgorithmModule {}
