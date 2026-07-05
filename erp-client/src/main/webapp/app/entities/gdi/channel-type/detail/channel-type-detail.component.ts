import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChannelType } from '../channel-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-channel-type-detail',
  templateUrl: './channel-type-detail.component.html',
})
export class ChannelTypeDetailComponent implements OnInit {
  channelType: IChannelType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ channelType }) => {
      this.channelType = channelType;
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
