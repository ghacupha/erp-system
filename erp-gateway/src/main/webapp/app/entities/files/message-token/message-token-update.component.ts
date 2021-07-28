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
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMessageToken, MessageToken } from 'app/shared/model/files/message-token.model';
import { MessageTokenService } from './message-token.service';

@Component({
  selector: 'jhi-message-token-update',
  templateUrl: './message-token-update.component.html',
})
export class MessageTokenUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    description: [],
    timeSent: [null, [Validators.required]],
    tokenValue: [null, [Validators.required]],
    received: [],
    actioned: [],
    contentFullyEnqueued: [],
  });

  constructor(protected messageTokenService: MessageTokenService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ messageToken }) => {
      this.updateForm(messageToken);
    });
  }

  updateForm(messageToken: IMessageToken): void {
    this.editForm.patchValue({
      id: messageToken.id,
      description: messageToken.description,
      timeSent: messageToken.timeSent,
      tokenValue: messageToken.tokenValue,
      received: messageToken.received,
      actioned: messageToken.actioned,
      contentFullyEnqueued: messageToken.contentFullyEnqueued,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const messageToken = this.createFromForm();
    if (messageToken.id !== undefined) {
      this.subscribeToSaveResponse(this.messageTokenService.update(messageToken));
    } else {
      this.subscribeToSaveResponse(this.messageTokenService.create(messageToken));
    }
  }

  private createFromForm(): IMessageToken {
    return {
      ...new MessageToken(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      timeSent: this.editForm.get(['timeSent'])!.value,
      tokenValue: this.editForm.get(['tokenValue'])!.value,
      received: this.editForm.get(['received'])!.value,
      actioned: this.editForm.get(['actioned'])!.value,
      contentFullyEnqueued: this.editForm.get(['contentFullyEnqueued'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMessageToken>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
