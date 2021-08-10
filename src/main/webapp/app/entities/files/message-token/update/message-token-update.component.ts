import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMessageToken, MessageToken } from '../message-token.model';
import { MessageTokenService } from '../service/message-token.service';

@Component({
  selector: 'gha-message-token-update',
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

  constructor(protected messageTokenService: MessageTokenService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ messageToken }) => {
      this.updateForm(messageToken);
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMessageToken>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(messageToken: IMessageToken): void {
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

  protected createFromForm(): IMessageToken {
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
}
