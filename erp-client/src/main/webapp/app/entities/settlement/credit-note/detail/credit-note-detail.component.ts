import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICreditNote } from '../credit-note.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-credit-note-detail',
  templateUrl: './credit-note-detail.component.html',
})
export class CreditNoteDetailComponent implements OnInit {
  creditNote: ICreditNote | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ creditNote }) => {
      this.creditNote = creditNote;
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
