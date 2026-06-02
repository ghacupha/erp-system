import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MfbBranchCodeComponent } from './list/mfb-branch-code.component';
import { MfbBranchCodeDetailComponent } from './detail/mfb-branch-code-detail.component';
import { MfbBranchCodeUpdateComponent } from './update/mfb-branch-code-update.component';
import { MfbBranchCodeDeleteDialogComponent } from './delete/mfb-branch-code-delete-dialog.component';
import { MfbBranchCodeRoutingModule } from './route/mfb-branch-code-routing.module';

@NgModule({
  imports: [SharedModule, MfbBranchCodeRoutingModule],
  declarations: [MfbBranchCodeComponent, MfbBranchCodeDetailComponent, MfbBranchCodeUpdateComponent, MfbBranchCodeDeleteDialogComponent],
  entryComponents: [MfbBranchCodeDeleteDialogComponent],
})
export class MfbBranchCodeModule {}
