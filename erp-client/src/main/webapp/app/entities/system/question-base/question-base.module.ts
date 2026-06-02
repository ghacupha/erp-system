import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { QuestionBaseComponent } from './list/question-base.component';
import { QuestionBaseDetailComponent } from './detail/question-base-detail.component';
import { QuestionBaseUpdateComponent } from './update/question-base-update.component';
import { QuestionBaseDeleteDialogComponent } from './delete/question-base-delete-dialog.component';
import { QuestionBaseRoutingModule } from './route/question-base-routing.module';

@NgModule({
  imports: [SharedModule, QuestionBaseRoutingModule],
  declarations: [QuestionBaseComponent, QuestionBaseDetailComponent, QuestionBaseUpdateComponent, QuestionBaseDeleteDialogComponent],
  entryComponents: [QuestionBaseDeleteDialogComponent],
})
export class QuestionBaseModule {}
