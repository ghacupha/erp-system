import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFileType } from '../file-type.model';
import { FileTypeService } from '../service/file-type.service';

@Component({
  templateUrl: './file-type-delete-dialog.component.html',
})
export class FileTypeDeleteDialogComponent {
  fileType?: IFileType;

  constructor(protected fileTypeService: FileTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fileTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
