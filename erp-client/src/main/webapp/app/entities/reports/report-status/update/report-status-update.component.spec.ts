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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReportStatusService } from '../service/report-status.service';
import { IReportStatus, ReportStatus } from '../report-status.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IProcessStatus } from 'app/entities/system/process-status/process-status.model';
import { ProcessStatusService } from 'app/entities/system/process-status/service/process-status.service';

import { ReportStatusUpdateComponent } from './report-status-update.component';

describe('ReportStatus Management Update Component', () => {
  let comp: ReportStatusUpdateComponent;
  let fixture: ComponentFixture<ReportStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportStatusService: ReportStatusService;
  let placeholderService: PlaceholderService;
  let processStatusService: ProcessStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ReportStatusUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ReportStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportStatusService = TestBed.inject(ReportStatusService);
    placeholderService = TestBed.inject(PlaceholderService);
    processStatusService = TestBed.inject(ProcessStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const reportStatus: IReportStatus = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 83138 }];
      reportStatus.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 41146 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportStatus });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ProcessStatus query and add missing value', () => {
      const reportStatus: IReportStatus = { id: 456 };
      const processStatus: IProcessStatus = { id: 54365 };
      reportStatus.processStatus = processStatus;

      const processStatusCollection: IProcessStatus[] = [{ id: 42210 }];
      jest.spyOn(processStatusService, 'query').mockReturnValue(of(new HttpResponse({ body: processStatusCollection })));
      const additionalProcessStatuses = [processStatus];
      const expectedCollection: IProcessStatus[] = [...additionalProcessStatuses, ...processStatusCollection];
      jest.spyOn(processStatusService, 'addProcessStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportStatus });
      comp.ngOnInit();

      expect(processStatusService.query).toHaveBeenCalled();
      expect(processStatusService.addProcessStatusToCollectionIfMissing).toHaveBeenCalledWith(
        processStatusCollection,
        ...additionalProcessStatuses
      );
      expect(comp.processStatusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reportStatus: IReportStatus = { id: 456 };
      const placeholders: IPlaceholder = { id: 18063 };
      reportStatus.placeholders = [placeholders];
      const processStatus: IProcessStatus = { id: 68314 };
      reportStatus.processStatus = processStatus;

      activatedRoute.data = of({ reportStatus });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(reportStatus));
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.processStatusesSharedCollection).toContain(processStatus);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReportStatus>>();
      const reportStatus = { id: 123 };
      jest.spyOn(reportStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportStatus }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportStatusService.update).toHaveBeenCalledWith(reportStatus);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReportStatus>>();
      const reportStatus = new ReportStatus();
      jest.spyOn(reportStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportStatus }));
      saveSubject.complete();

      // THEN
      expect(reportStatusService.create).toHaveBeenCalledWith(reportStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReportStatus>>();
      const reportStatus = { id: 123 };
      jest.spyOn(reportStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportStatusService.update).toHaveBeenCalledWith(reportStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPlaceholderById', () => {
      it('Should return tracked Placeholder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlaceholderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackProcessStatusById', () => {
      it('Should return tracked ProcessStatus primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProcessStatusById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedPlaceholder', () => {
      it('Should return option if no Placeholder is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPlaceholder(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Placeholder for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPlaceholder(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Placeholder is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPlaceholder(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
