import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TerminalFunctionsComponent } from './list/terminal-functions.component';
import { TerminalFunctionsDetailComponent } from './detail/terminal-functions-detail.component';
import { TerminalFunctionsUpdateComponent } from './update/terminal-functions-update.component';
import { TerminalFunctionsDeleteDialogComponent } from './delete/terminal-functions-delete-dialog.component';
import { TerminalFunctionsRoutingModule } from './route/terminal-functions-routing.module';

@NgModule({
  imports: [SharedModule, TerminalFunctionsRoutingModule],
  declarations: [
    TerminalFunctionsComponent,
    TerminalFunctionsDetailComponent,
    TerminalFunctionsUpdateComponent,
    TerminalFunctionsDeleteDialogComponent,
  ],
  entryComponents: [TerminalFunctionsDeleteDialogComponent],
})
export class TerminalFunctionsModule {}
