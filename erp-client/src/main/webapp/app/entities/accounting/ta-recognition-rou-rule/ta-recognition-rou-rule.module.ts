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
