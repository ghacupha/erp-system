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
import { ErpCommonModule } from '../../erp-common/erp-common.module';
import { LeaseLiabilityScheduleUploadComponent } from './lease-liability-schedule-upload.component';
import { LeaseLiabilityScheduleUploadRoutingModule } from './route/lease-liability-schedule-upload-routing.module';
import { LeaseLiabilityFormComponentsModule } from '../../erp-common/form-components/lease-liability-components/lease-liability-form-components.module';
import { LeaseAmortizationScheduleFormComponentsModule } from '../../erp-common/form-components/lease-amortization-schedule-components/lease-amortization-schedule-form-components.module';
import { LeaseLiabilityCompilationFormComponentsModule } from '../../erp-common/form-components/lease-liability-compilation-components/lease-liability-compilation-form-components.module';
import { Ifrs16LeaseContractComponentsModule } from '../../erp-common/form-components/ifrs16-lease-contract-components/ifrs-16-lease-contract-components.module';
import { CsvFileUploadFormComponentsModule } from '../../erp-common/form-components/csv-file-upload-components/csv-file-upload-form-components.module';

@NgModule({
  imports: [
    SharedModule,
    ErpCommonModule,
    LeaseLiabilityFormComponentsModule,
    LeaseAmortizationScheduleFormComponentsModule,
    LeaseLiabilityCompilationFormComponentsModule,
    Ifrs16LeaseContractComponentsModule,
    CsvFileUploadFormComponentsModule,
    LeaseLiabilityScheduleUploadRoutingModule,
  ],
  declarations: [LeaseLiabilityScheduleUploadComponent],
})
export class LeaseLiabilityScheduleUploadModule {}
