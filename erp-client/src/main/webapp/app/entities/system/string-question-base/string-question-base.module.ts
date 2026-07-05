import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StringQuestionBaseComponent } from './list/string-question-base.component';
import { StringQuestionBaseDetailComponent } from './detail/string-question-base-detail.component';
import { StringQuestionBaseUpdateComponent } from './update/string-question-base-update.component';
import { StringQuestionBaseDeleteDialogComponent } from './delete/string-question-base-delete-dialog.component';
import { StringQuestionBaseRoutingModule } from './route/string-question-base-routing.module';

@NgModule({
  imports: [SharedModule, StringQuestionBaseRoutingModule],
  declarations: [
    StringQuestionBaseComponent,
    StringQuestionBaseDetailComponent,
    StringQuestionBaseUpdateComponent,
    StringQuestionBaseDeleteDialogComponent,
  ],
  entryComponents: [StringQuestionBaseDeleteDialogComponent],
})
export class StringQuestionBaseModule {}
