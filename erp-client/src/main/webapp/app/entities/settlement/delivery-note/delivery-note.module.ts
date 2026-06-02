import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeliveryNoteComponent } from './list/delivery-note.component';
import { DeliveryNoteDetailComponent } from './detail/delivery-note-detail.component';
import { DeliveryNoteUpdateComponent } from './update/delivery-note-update.component';
import { DeliveryNoteDeleteDialogComponent } from './delete/delivery-note-delete-dialog.component';
import { DeliveryNoteRoutingModule } from './route/delivery-note-routing.module';

@NgModule({
  imports: [SharedModule, DeliveryNoteRoutingModule],
  declarations: [DeliveryNoteComponent, DeliveryNoteDetailComponent, DeliveryNoteUpdateComponent, DeliveryNoteDeleteDialogComponent],
  entryComponents: [DeliveryNoteDeleteDialogComponent],
})
export class DeliveryNoteModule {}
