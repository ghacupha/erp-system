import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDepreciationJobNotice } from '../depreciation-job-notice.model';
import { DepreciationJobNoticeService } from '../service/depreciation-job-notice.service';

@Component({
  templateUrl: './depreciation-job-notice-delete-dialog.component.html',
})
export class DepreciationJobNoticeDeleteDialogComponent {
  depreciationJobNotice?: IDepreciationJobNotice;

  constructor(protected depreciationJobNoticeService: DepreciationJobNoticeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.depreciationJobNoticeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
