import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TerminalTypesComponent } from './list/terminal-types.component';
import { TerminalTypesDetailComponent } from './detail/terminal-types-detail.component';
import { TerminalTypesUpdateComponent } from './update/terminal-types-update.component';
import { TerminalTypesDeleteDialogComponent } from './delete/terminal-types-delete-dialog.component';
import { TerminalTypesRoutingModule } from './route/terminal-types-routing.module';

@NgModule({
  imports: [SharedModule, TerminalTypesRoutingModule],
  declarations: [TerminalTypesComponent, TerminalTypesDetailComponent, TerminalTypesUpdateComponent, TerminalTypesDeleteDialogComponent],
  entryComponents: [TerminalTypesDeleteDialogComponent],
})
export class TerminalTypesModule {}
