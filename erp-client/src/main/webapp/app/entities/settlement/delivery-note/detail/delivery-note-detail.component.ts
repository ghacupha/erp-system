import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeliveryNote } from '../delivery-note.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-delivery-note-detail',
  templateUrl: './delivery-note-detail.component.html',
})
export class DeliveryNoteDetailComponent implements OnInit {
  deliveryNote: IDeliveryNote | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliveryNote }) => {
      this.deliveryNote = deliveryNote;
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
