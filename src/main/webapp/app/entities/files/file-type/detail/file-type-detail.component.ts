import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFileType } from '../file-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'gha-file-type-detail',
  templateUrl: './file-type-detail.component.html',
})
export class FileTypeDetailComponent implements OnInit {
  fileType: IFileType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileType }) => {
      this.fileType = fileType;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
