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
import { TALeaseRecognitionRuleComponent } from './list/ta-lease-recognition-rule.component';
import { TALeaseRecognitionRuleDetailComponent } from './detail/ta-lease-recognition-rule-detail.component';
import { TALeaseRecognitionRuleUpdateComponent } from './update/ta-lease-recognition-rule-update.component';
import { TALeaseRecognitionRuleDeleteDialogComponent } from './delete/ta-lease-recognition-rule-delete-dialog.component';
import { TALeaseRecognitionRuleRoutingModule } from './route/ta-lease-recognition-rule-routing.module';
import { ErpCommonModule } from '../../erp-common/erp-common.module';
import { TALeaseRecognitionRuleRoutingCustomModule } from './route/ta-lease-recognition-rule-routing-custom.module';

@NgModule({
  imports: [SharedModule, TALeaseRecognitionRuleRoutingModule, TALeaseRecognitionRuleRoutingCustomModule, ErpCommonModule],
  declarations: [
    TALeaseRecognitionRuleComponent,
    TALeaseRecognitionRuleDetailComponent,
    TALeaseRecognitionRuleUpdateComponent,
    TALeaseRecognitionRuleDeleteDialogComponent,
  ],
  entryComponents: [TALeaseRecognitionRuleDeleteDialogComponent],
})
export class TALeaseRecognitionRuleModule {}
