///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IExcelReportExport, ExcelReportExport } from '../excel-report-export.model';
import { ExcelReportExportService } from '../service/excel-report-export.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { IUniversallyUniqueMapping } from '../../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';
import { IReportStatus } from '../../report-status/report-status.model';
import { ISecurityClearance } from '../../../erp-pages/security-clearance/security-clearance.model';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { IDealer } from '../../../erp-pages/dealers/dealer/dealer.model';
import { ISystemModule } from '../../../erp-pages/system-module/system-module.model';
import { IReportDesign } from '../../report-design/report-design.model';
import { IAlgorithm } from '../../../erp-pages/algorithm/algorithm.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { UniversallyUniqueMappingService } from '../../../erp-pages/universally-unique-mapping/service/universally-unique-mapping.service';
import { SecurityClearanceService } from '../../../erp-pages/security-clearance/service/security-clearance.service';
import { ReportStatusService } from '../../report-status/service/report-status.service';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';
import { DealerService } from '../../../erp-pages/dealers/dealer/service/dealer.service';
import { SystemModuleService } from '../../../erp-pages/system-module/service/system-module.service';
import { ReportDesignService } from '../../report-design/service/report-design.service';
import { AlgorithmService } from '../../../erp-pages/algorithm/service/algorithm.service';
import { v4 as uuidv4 } from 'uuid';
import { SearchWithPagination } from '../../../../core/request/request.model';

@Component({
  selector: 'jhi-excel-report-export-update',
  templateUrl: './excel-report-export-update.component.html',
})
export class ExcelReportExportUpdateComponent implements OnInit {
  isSaving = false;

  placeholdersSharedCollection: IPlaceholder[] = [];
  universallyUniqueMappingsSharedCollection: IUniversallyUniqueMapping[] = [];
  reportStatusesCollection: IReportStatus[] = [];
  securityClearancesSharedCollection: ISecurityClearance[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];
  dealersSharedCollection: IDealer[] = [];
  systemModulesSharedCollection: ISystemModule[] = [];
  reportDesignsSharedCollection: IReportDesign[] = [];
  algorithmsSharedCollection: IAlgorithm[] = [];

  editForm = this.fb.group({
    id: [],
    reportName: [null, [Validators.required]],
    reportPassword: [null, [Validators.required]],
    reportNotes: [],
    reportNotesContentType: [],
    fileCheckSum: [],
    reportFile: [],
    reportFileContentType: [],
    reportTimeStamp: [null, [Validators.required]],
    reportId: [null, [Validators.required]],
    placeholders: [],
    parameters: [],
    reportStatus: [],
    securityClearance: [null, Validators.required],
    reportCreator: [null, Validators.required],
    organization: [null, Validators.required],
    department: [null, Validators.required],
    systemModule: [null, Validators.required],
    reportDesign: [null, Validators.required],
    fileCheckSumAlgorithm: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected excelReportExportService: ExcelReportExportService,
    protected placeholderService: PlaceholderService,
    protected universallyUniqueMappingService: UniversallyUniqueMappingService,
    protected reportStatusService: ReportStatusService,
    protected securityClearanceService: SecurityClearanceService,
    protected applicationUserService: ApplicationUserService,
    protected dealerService: DealerService,
    protected systemModuleService: SystemModuleService,
    protected reportDesignService: ReportDesignService,
    protected algorithmService: AlgorithmService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ excelReportExport }) => {
      if (excelReportExport.id === undefined) {
        const today = dayjs();
        excelReportExport.reportTimeStamp = today;
      }

      this.updateForm(excelReportExport);

      this.loadRelationshipsOptions();
      this.updatePreferredOrganization();
      this.updatePreferredReportCreator();
      this.updatePreferredSystemModule();
      this.updatePreferredFileChecksumAlgorithm();

      // TODO Check how this will conflict with the user security-clearance
      this.updatePreferredSecurityClearance();

    });

    this.editForm.patchValue({
      reportId: uuidv4(),
    })

    this.updatePreferredDepartment();
  }

  updatePreferredDepartment(): void {
    this.universallyUniqueMappingService.findMap("globallyPreferredExcelExportUpdateDepartment")
      .subscribe((mapped) => {
        this.dealerService.search(<SearchWithPagination>{ page: 0, size: 0, sort: [], query: mapped.body?.mappedValue })
          .subscribe(({ body: vals }) => {
            if (vals) {
              this.editForm.patchValue({
                department: vals[0]
              });
            }
          });
      });
  }

  updatePreferredOrganization(): void {
    this.universallyUniqueMappingService.findMap("globallyPreferredExcelExportUpdateOrganization")
      .subscribe((mapped) => {
        this.dealerService.search(<SearchWithPagination>{ page: 0, size: 0, sort: [], query: mapped.body?.mappedValue })
          .subscribe(({ body: vals }) => {
            if (vals) {
              this.editForm.patchValue({
                organization: vals[0]
              });
            }
          });
      });
  }

  updatePreferredReportCreator(): void {
    this.universallyUniqueMappingService.findMap("globallyPreferredExcelExportUpdateCreator")
      .subscribe((mapped) => {
        this.applicationUserService.search(<SearchWithPagination>{ page: 0, size: 0, sort: [], query: mapped.body?.mappedValue })
          .subscribe(({ body: vals }) => {
            if (vals) {
              this.editForm.patchValue({
                reportCreator: vals[0]
              });
            }
          });
      });
  }

  updatePreferredBillerGivenInvoice(): void {
    this.editForm.get(['paymentInvoices'])?.valueChanges.subscribe((invoices) => {
      const p_dealers: IDealer[] = [];
      invoices.forEach((inv: { biller: IDealer; }) => {
        p_dealers.push(inv.biller);
      })
      this.editForm.get(['biller'])?.setValue(p_dealers[0])
    });
  }

  updatePreferredSystemModule(): void {
    this.universallyUniqueMappingService.findMap("globallyPreferredExcelExportUpdateErpModule")
      .subscribe((mapped) => {
        this.systemModuleService.search(<SearchWithPagination>{ page: 0, size: 0, sort: [], query: mapped.body?.mappedValue })
          .subscribe(({ body: vals }) => {
            if (vals) {
              this.editForm.patchValue({
                systemModule: vals[0]
              });
            }
          });
      });
  }

  updatePreferredSecurityClearance(): void {
    this.universallyUniqueMappingService.findMap("globallyPreferredExcelExportUpdateSecurityClearance")
      .subscribe((mapped) => {
        this.securityClearanceService.search(<SearchWithPagination>{ page: 0, size: 0, sort: [], query: mapped.body?.mappedValue })
          .subscribe(({ body: vals }) => {
            if (vals) {
              this.editForm.patchValue({
                securityClearance: vals[0]
              });
            }
          });
      });
  }

  updatePreferredFileChecksumAlgorithm(): void {
    this.universallyUniqueMappingService.findMap("globallyPreferredExcelExportUpdateFileChecksumAlgorithm")
      .subscribe((mapped) => {
        this.algorithmService.search(<SearchWithPagination>{ page: 0, size: 0, sort: [], query: mapped.body?.mappedValue })
          .subscribe(({ body: vals }) => {
            if (vals) {
              this.editForm.patchValue({
                fileCheckSumAlgorithm: vals[0]
              });
            }
          });
      });
  }

  updateApplicationUser(update: IApplicationUser): void {
    this.editForm.patchValue({
      reportCreator: update
    });
  }

  updateParameters(update: IUniversallyUniqueMapping[]): void {
    this.editForm.patchValue({
      parameters: [...update]
    })
  }

  updateSecurityClearance(update: ISecurityClearance): void {
    this.editForm.patchValue({
      securityClearance: update
    })
  }

  updatePlaceholders(update: IPlaceholder[]): void {
    this.editForm.patchValue({
      placeholders: [...update]
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateOrganization(dealerUpdate: IDealer): void {
    this.editForm.patchValue({
      organization: dealerUpdate,
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateReportDesign(reportDesign: IReportDesign): void {
    this.editForm.patchValue({
      reportDesign,
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateDepartment(dealerUpdate: IDealer): void {
    this.editForm.patchValue({
      department: dealerUpdate,
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateFileChecksumAlgorithm(algorithmSelection: IAlgorithm): void {
    this.editForm.patchValue({
      fileCheckSum: algorithmSelection,
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
  updateSystemModule(systemModule: ISystemModule): void {
    this.editForm.patchValue({
      systemModule,
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
        this.eventManager.broadcast(new EventWithContent<AlertError>('erpSystemApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const excelReportExport = this.createFromForm();
    if (excelReportExport.id !== undefined) {
      this.subscribeToSaveResponse(this.excelReportExportService.update(excelReportExport));
    } else {
      this.subscribeToSaveResponse(this.excelReportExportService.create(excelReportExport));
    }
  }

  trackPlaceholderById(index: number, item: IPlaceholder): number {
    return item.id!;
  }

  trackUniversallyUniqueMappingById(index: number, item: IUniversallyUniqueMapping): number {
    return item.id!;
  }

  trackReportStatusById(index: number, item: IReportStatus): number {
    return item.id!;
  }

  trackSecurityClearanceById(index: number, item: ISecurityClearance): number {
    return item.id!;
  }

  trackApplicationUserById(index: number, item: IApplicationUser): number {
    return item.id!;
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  trackSystemModuleById(index: number, item: ISystemModule): number {
    return item.id!;
  }

  trackReportDesignById(index: number, item: IReportDesign): number {
    return item.id!;
  }

  trackAlgorithmById(index: number, item: IAlgorithm): number {
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

  getSelectedUniversallyUniqueMapping(
    option: IUniversallyUniqueMapping,
    selectedVals?: IUniversallyUniqueMapping[]
  ): IUniversallyUniqueMapping {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExcelReportExport>>): void {
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

  protected updateForm(excelReportExport: IExcelReportExport): void {
    this.editForm.patchValue({
      id: excelReportExport.id,
      reportName: excelReportExport.reportName,
      reportPassword: excelReportExport.reportPassword,
      reportNotes: excelReportExport.reportNotes,
      reportNotesContentType: excelReportExport.reportNotesContentType,
      fileCheckSum: excelReportExport.fileCheckSum,
      reportFile: excelReportExport.reportFile,
      reportFileContentType: excelReportExport.reportFileContentType,
      reportTimeStamp: excelReportExport.reportTimeStamp ? excelReportExport.reportTimeStamp.format(DATE_TIME_FORMAT) : null,
      reportId: excelReportExport.reportId,
      placeholders: excelReportExport.placeholders,
      parameters: excelReportExport.parameters,
      reportStatus: excelReportExport.reportStatus,
      securityClearance: excelReportExport.securityClearance,
      reportCreator: excelReportExport.reportCreator,
      organization: excelReportExport.organization,
      department: excelReportExport.department,
      systemModule: excelReportExport.systemModule,
      reportDesign: excelReportExport.reportDesign,
      fileCheckSumAlgorithm: excelReportExport.fileCheckSumAlgorithm,
    });

    this.placeholdersSharedCollection = this.placeholderService.addPlaceholderToCollectionIfMissing(
      this.placeholdersSharedCollection,
      ...(excelReportExport.placeholders ?? [])
    );
    this.universallyUniqueMappingsSharedCollection = this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
      this.universallyUniqueMappingsSharedCollection,
      ...(excelReportExport.parameters ?? [])
    );
    this.reportStatusesCollection = this.reportStatusService.addReportStatusToCollectionIfMissing(
      this.reportStatusesCollection,
      excelReportExport.reportStatus
    );
    this.securityClearancesSharedCollection = this.securityClearanceService.addSecurityClearanceToCollectionIfMissing(
      this.securityClearancesSharedCollection,
      excelReportExport.securityClearance
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing(
      this.applicationUsersSharedCollection,
      excelReportExport.reportCreator
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(
      this.dealersSharedCollection,
      excelReportExport.organization,
      excelReportExport.department
    );
    this.systemModulesSharedCollection = this.systemModuleService.addSystemModuleToCollectionIfMissing(
      this.systemModulesSharedCollection,
      excelReportExport.systemModule
    );
    this.reportDesignsSharedCollection = this.reportDesignService.addReportDesignToCollectionIfMissing(
      this.reportDesignsSharedCollection,
      excelReportExport.reportDesign
    );
    this.algorithmsSharedCollection = this.algorithmService.addAlgorithmToCollectionIfMissing(
      this.algorithmsSharedCollection,
      excelReportExport.fileCheckSumAlgorithm
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

    this.universallyUniqueMappingService
      .query()
      .pipe(map((res: HttpResponse<IUniversallyUniqueMapping[]>) => res.body ?? []))
      .pipe(
        map((universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          this.universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing(
            universallyUniqueMappings,
            ...(this.editForm.get('parameters')!.value ?? [])
          )
        )
      )
      .subscribe(
        (universallyUniqueMappings: IUniversallyUniqueMapping[]) =>
          (this.universallyUniqueMappingsSharedCollection = universallyUniqueMappings)
      );

    this.reportStatusService
      .query({ 'excelReportExportId.specified': 'false' })
      .pipe(map((res: HttpResponse<IReportStatus[]>) => res.body ?? []))
      .pipe(
        map((reportStatuses: IReportStatus[]) =>
          this.reportStatusService.addReportStatusToCollectionIfMissing(reportStatuses, this.editForm.get('reportStatus')!.value)
        )
      )
      .subscribe((reportStatuses: IReportStatus[]) => (this.reportStatusesCollection = reportStatuses));

    this.securityClearanceService
      .query()
      .pipe(map((res: HttpResponse<ISecurityClearance[]>) => res.body ?? []))
      .pipe(
        map((securityClearances: ISecurityClearance[]) =>
          this.securityClearanceService.addSecurityClearanceToCollectionIfMissing(
            securityClearances,
            this.editForm.get('securityClearance')!.value
          )
        )
      )
      .subscribe((securityClearances: ISecurityClearance[]) => (this.securityClearancesSharedCollection = securityClearances));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing(applicationUsers, this.editForm.get('reportCreator')!.value)
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(
        map((dealers: IDealer[]) =>
          this.dealerService.addDealerToCollectionIfMissing(
            dealers,
            this.editForm.get('organization')!.value,
            this.editForm.get('department')!.value
          )
        )
      )
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));

    this.systemModuleService
      .query()
      .pipe(map((res: HttpResponse<ISystemModule[]>) => res.body ?? []))
      .pipe(
        map((systemModules: ISystemModule[]) =>
          this.systemModuleService.addSystemModuleToCollectionIfMissing(systemModules, this.editForm.get('systemModule')!.value)
        )
      )
      .subscribe((systemModules: ISystemModule[]) => (this.systemModulesSharedCollection = systemModules));

    this.reportDesignService
      .query()
      .pipe(map((res: HttpResponse<IReportDesign[]>) => res.body ?? []))
      .pipe(
        map((reportDesigns: IReportDesign[]) =>
          this.reportDesignService.addReportDesignToCollectionIfMissing(reportDesigns, this.editForm.get('reportDesign')!.value)
        )
      )
      .subscribe((reportDesigns: IReportDesign[]) => (this.reportDesignsSharedCollection = reportDesigns));

    this.algorithmService
      .query()
      .pipe(map((res: HttpResponse<IAlgorithm[]>) => res.body ?? []))
      .pipe(
        map((algorithms: IAlgorithm[]) =>
          this.algorithmService.addAlgorithmToCollectionIfMissing(algorithms, this.editForm.get('fileCheckSumAlgorithm')!.value)
        )
      )
      .subscribe((algorithms: IAlgorithm[]) => (this.algorithmsSharedCollection = algorithms));
  }

  protected createFromForm(): IExcelReportExport {
    return {
      ...new ExcelReportExport(),
      id: this.editForm.get(['id'])!.value,
      reportName: this.editForm.get(['reportName'])!.value,
      reportPassword: this.editForm.get(['reportPassword'])!.value,
      reportNotesContentType: this.editForm.get(['reportNotesContentType'])!.value,
      reportNotes: this.editForm.get(['reportNotes'])!.value,
      fileCheckSum: this.editForm.get(['fileCheckSum'])!.value,
      reportFileContentType: this.editForm.get(['reportFileContentType'])!.value,
      reportFile: this.editForm.get(['reportFile'])!.value,
      reportTimeStamp: this.editForm.get(['reportTimeStamp'])!.value
        ? dayjs(this.editForm.get(['reportTimeStamp'])!.value, DATE_TIME_FORMAT)
        : undefined,
      reportId: this.editForm.get(['reportId'])!.value,
      placeholders: this.editForm.get(['placeholders'])!.value,
      parameters: this.editForm.get(['parameters'])!.value,
      reportStatus: this.editForm.get(['reportStatus'])!.value,
      securityClearance: this.editForm.get(['securityClearance'])!.value,
      reportCreator: this.editForm.get(['reportCreator'])!.value,
      organization: this.editForm.get(['organization'])!.value,
      department: this.editForm.get(['department'])!.value,
      systemModule: this.editForm.get(['systemModule'])!.value,
      reportDesign: this.editForm.get(['reportDesign'])!.value,
      fileCheckSumAlgorithm: this.editForm.get(['fileCheckSumAlgorithm'])!.value,
    };
  }
}
