import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChartOfAccountsCodeComponent } from './list/chart-of-accounts-code.component';
import { ChartOfAccountsCodeDetailComponent } from './detail/chart-of-accounts-code-detail.component';
import { ChartOfAccountsCodeUpdateComponent } from './update/chart-of-accounts-code-update.component';
import { ChartOfAccountsCodeDeleteDialogComponent } from './delete/chart-of-accounts-code-delete-dialog.component';
import { ChartOfAccountsCodeRoutingModule } from './route/chart-of-accounts-code-routing.module';

@NgModule({
  imports: [SharedModule, ChartOfAccountsCodeRoutingModule],
  declarations: [
    ChartOfAccountsCodeComponent,
    ChartOfAccountsCodeDetailComponent,
    ChartOfAccountsCodeUpdateComponent,
    ChartOfAccountsCodeDeleteDialogComponent,
  ],
  entryComponents: [ChartOfAccountsCodeDeleteDialogComponent],
})
export class ChartOfAccountsCodeModule {}
