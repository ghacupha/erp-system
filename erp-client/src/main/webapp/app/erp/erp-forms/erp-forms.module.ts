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
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DealerMaintenanceModule } from './dealer-maintenance/dealer-maintenance.module';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
 imports: [
   FormsModule,
   ReactiveFormsModule,
   DealerMaintenanceModule,
   SharedModule,
   RouterModule.forChild([
       {
         path: 'erp/forms',
         data: { pageTitle: 'ERP | Dealer Maintenance' },
         loadChildren: () => import('./dealer-maintenance/dealer-maintenance.module').then(m => m.DealerMaintenanceModule),
       },
       {
         path: 'string-question-base',
         data: { pageTitle: 'StringQuestionBases' },
         loadChildren: () => import('./string-question-base/string-question-base.module').then(m => m.StringQuestionBaseModule),
       },
     ]),
  ],
  exports: [
    DealerMaintenanceModule,
  ]
})
export class ErpFormsModule {}
