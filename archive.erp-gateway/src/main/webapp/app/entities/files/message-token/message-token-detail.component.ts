import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMessageToken } from 'app/shared/model/files/message-token.model';

@Component({
  selector: 'jhi-message-token-detail',
  templateUrl: './message-token-detail.component.html',
})
export class MessageTokenDetailComponent implements OnInit {
  messageToken: IMessageToken | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ messageToken }) => (this.messageToken = messageToken));
  }

  previousState(): void {
    window.history.back();
  }
}
