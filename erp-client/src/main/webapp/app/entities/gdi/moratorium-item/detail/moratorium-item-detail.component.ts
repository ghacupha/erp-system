import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMoratoriumItem } from '../moratorium-item.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-moratorium-item-detail',
  templateUrl: './moratorium-item-detail.component.html',
})
export class MoratoriumItemDetailComponent implements OnInit {
  moratoriumItem: IMoratoriumItem | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moratoriumItem }) => {
      this.moratoriumItem = moratoriumItem;
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
