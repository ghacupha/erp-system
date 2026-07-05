import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CrbGlCodeComponent } from './list/crb-gl-code.component';
import { CrbGlCodeDetailComponent } from './detail/crb-gl-code-detail.component';
import { CrbGlCodeUpdateComponent } from './update/crb-gl-code-update.component';
import { CrbGlCodeDeleteDialogComponent } from './delete/crb-gl-code-delete-dialog.component';
import { CrbGlCodeRoutingModule } from './route/crb-gl-code-routing.module';

@NgModule({
  imports: [SharedModule, CrbGlCodeRoutingModule],
  declarations: [CrbGlCodeComponent, CrbGlCodeDetailComponent, CrbGlCodeUpdateComponent, CrbGlCodeDeleteDialogComponent],
  entryComponents: [CrbGlCodeDeleteDialogComponent],
})
export class CrbGlCodeModule {}
