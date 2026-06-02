import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SnaSectorCodeComponent } from './list/sna-sector-code.component';
import { SnaSectorCodeDetailComponent } from './detail/sna-sector-code-detail.component';
import { SnaSectorCodeUpdateComponent } from './update/sna-sector-code-update.component';
import { SnaSectorCodeDeleteDialogComponent } from './delete/sna-sector-code-delete-dialog.component';
import { SnaSectorCodeRoutingModule } from './route/sna-sector-code-routing.module';

@NgModule({
  imports: [SharedModule, SnaSectorCodeRoutingModule],
  declarations: [SnaSectorCodeComponent, SnaSectorCodeDetailComponent, SnaSectorCodeUpdateComponent, SnaSectorCodeDeleteDialogComponent],
  entryComponents: [SnaSectorCodeDeleteDialogComponent],
})
export class SnaSectorCodeModule {}
