import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChartOfAccountsCode } from '../chart-of-accounts-code.model';
import { ChartOfAccountsCodeService } from '../service/chart-of-accounts-code.service';

@Component({
  templateUrl: './chart-of-accounts-code-delete-dialog.component.html',
})
export class ChartOfAccountsCodeDeleteDialogComponent {
  chartOfAccountsCode?: IChartOfAccountsCode;

  constructor(protected chartOfAccountsCodeService: ChartOfAccountsCodeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chartOfAccountsCodeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
