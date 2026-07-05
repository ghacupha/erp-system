import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TALeaseRecognitionRuleComponent } from './list/ta-lease-recognition-rule.component';
import { TALeaseRecognitionRuleDetailComponent } from './detail/ta-lease-recognition-rule-detail.component';
import { TALeaseRecognitionRuleUpdateComponent } from './update/ta-lease-recognition-rule-update.component';
import { TALeaseRecognitionRuleDeleteDialogComponent } from './delete/ta-lease-recognition-rule-delete-dialog.component';
import { TALeaseRecognitionRuleRoutingModule } from './route/ta-lease-recognition-rule-routing.module';

@NgModule({
  imports: [SharedModule, TALeaseRecognitionRuleRoutingModule],
  declarations: [
    TALeaseRecognitionRuleComponent,
    TALeaseRecognitionRuleDetailComponent,
    TALeaseRecognitionRuleUpdateComponent,
    TALeaseRecognitionRuleDeleteDialogComponent,
  ],
  entryComponents: [TALeaseRecognitionRuleDeleteDialogComponent],
})
export class TALeaseRecognitionRuleModule {}
