///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

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
