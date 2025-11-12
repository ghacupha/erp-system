///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IReportTemplate, ReportTemplate } from '../report-template.model';
import { ReportTemplateService } from '../service/report-template.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPlaceholder, Placeholder } from '../../../erp-pages/placeholder/placeholder.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { sha512, md5 } from 'hash-wasm';
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'jhi-report-template-update',
  templateUrl: './report-template-update.component.html'
})
export class ReportTemplateUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];

  checkSumPlaceholder: IPlaceholder | null = {};

  catalogueToken = '';
  fileToken = '';

  editForm = this.fb.group({
    id: [],
    catalogueNumber: [null, [Validators.required]],
    description: [],
    notes: [],
    notesContentType: [],
    reportFile: [],
    reportFileContentType: [],
    compileReportFile: [],
    compileReportFileContentType: [],
    placeholders: []
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected reportTemplateService: ReportTemplateService,
    protected placeholderService: PlaceholderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportTemplate }) => {
      this.updateForm(reportTemplate);

      this.loadRelationshipsOptions();
    });

    md5(uuidv4()).then(token => {
      this.catalogueToken = token.substring(0, 6);
      this.editForm.patchValue({
        catalogueNumber: token.substring(0, 6)
      });
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('erpSystemApp.error', { message: err.message }))
    });
    sha512(this.editForm.get(['reportFile'])!.value).then(ftk => {
      this.fileToken = ftk;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportTemplate = this.createFromForm();
    if (reportTemplate.id !== undefined) {
      this.subscribeToSaveResponse(this.reportTemplateService.update(reportTemplate));
    } else {
      this.subscribeToSaveResponse(this.reportTemplateService.create(reportTemplate));
    }
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  getSelectedPlaceholder(option: IPlaceholder, selectedVals?: IPlaceholder[]): IPlaceholder {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportTemplate>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      (res) => this.onSaveSuccess(res),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(res: HttpResponse<IReportTemplate>): void {

    if (res.body?.reportFile) {
      const rpt: IReportTemplate = res.body;

      sha512(res.body.reportFile).then(tkn => {
        this.placeholderService.create({
          ...new Placeholder(),
          description: res.body?.catalogueNumber,
          token: tkn
        }).subscribe(plc => {
          if (plc.body) {
            rpt.placeholders?.push(plc.body);
          }
          this.reportTemplateService.update(rpt).subscribe(() => {
            this.isSaving = false;
          });
        });
      });
    }

    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    // this.isSaving = false;
  }

  protected updateForm(reportTemplate: IReportTemplate): void {
    this.editForm.patchValue({
      id: reportTemplate.id,
      catalogueNumber: reportTemplate.catalogueNumber,
      description: reportTemplate.description,
      notes: reportTemplate.notes,
      notesContentType: reportTemplate.notesContentType,
      reportFile: reportTemplate.reportFile,
      reportFileContentType: reportTemplate.reportFileContentType,
      compileReportFile: reportTemplate.compileReportFile,
      compileReportFileContentType: reportTemplate.compileReportFileContentType,
      placeholders: reportTemplate.placeholders
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(reportTemplate.placeholders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.placeholderService
      .query()
      .pipe(map((res: HttpResponse<IPlaceholder[]>) => res.body ?? []))
      .pipe(
        map((placeholders: IPlaceholder[]) =>
          this.placeholderService.addPlaceholderToCollectionIfMissing(placeholders, ...(this.editForm.get('placeholders')!.value ?? []))
        )
      )
      .subscribe((placeholders: IPlaceholder[]) => (this.placeholdersSharedCollection = placeholders));
  }

  protected createFromForm(): IReportTemplate {

    // sha512(this.editForm.get(['reportFile'])!.value).then(token => {
    //   this.placeholderService.create({
    //     description: this.catalogueToken,
    //     token,
    //   }).subscribe(pl => {
    //     // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    //     if (pl) {
    //       this.checkSumPlaceholder = pl.body;
    //     }
    //   });
    // });

    return {
      ...new ReportTemplate(),
      id: this.editForm.get(['id'])!.value,
      catalogueNumber: this.editForm.get(['catalogueNumber'])!.value,
      description: this.editForm.get(['description'])!.value,
      notesContentType: this.editForm.get(['notesContentType'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      reportFileContentType: this.editForm.get(['reportFileContentType'])!.value,
      reportFile: this.editForm.get(['reportFile'])!.value,
      compileReportFileContentType: this.editForm.get(['compileReportFileContentType'])!.value,
      compileReportFile: this.editForm.get(['compileReportFile'])!.value,
      placeholders: [this.editForm.get(['placeholders'])!.value]
    };
  }
}
