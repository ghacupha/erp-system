import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMessageToken } from '../message-token.model';

@Component({
  selector: 'gha-message-token-detail',
  templateUrl: './message-token-detail.component.html',
})
export class MessageTokenDetailComponent implements OnInit {
  messageToken: IMessageToken | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ messageToken }) => {
      this.messageToken = messageToken;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
