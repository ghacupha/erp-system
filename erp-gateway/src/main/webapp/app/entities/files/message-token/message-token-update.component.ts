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
