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

import { ReportTemplateService } from '../../report-template/service/report-template.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { XlsxReportRequisitionService } from '../service/xlsx-report-requisition.service';
import { IXlsxReportRequisition, XlsxReportRequisition } from '../xlsx-report-requisition.model';

import { XlsxReportRequisitionUpdateComponent } from './xlsx-report-requisition-update.component';
import { IReportTemplate } from '../../report-template/report-template.model';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';

describe('XlsxReportRequisition Management Update Component', () => {
  let comp: XlsxReportRequisitionUpdateComponent;
  let fixture: ComponentFixture<XlsxReportRequisitionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let xlsxReportRequisitionService: XlsxReportRequisitionService;
  let reportTemplateService: ReportTemplateService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [XlsxReportRequisitionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(XlsxReportRequisitionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(XlsxReportRequisitionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    xlsxReportRequisitionService = TestBed.inject(XlsxReportRequisitionService);
    reportTemplateService = TestBed.inject(ReportTemplateService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ReportTemplate query and add missing value', () => {
      const xlsxReportRequisition: IXlsxReportRequisition = { id: 456 };
      const reportTemplate: IReportTemplate = { id: 46556 };
      xlsxReportRequisition.reportTemplate = reportTemplate;

      const reportTemplateCollection: IReportTemplate[] = [{ id: 31178 }];
      jest.spyOn(reportTemplateService, 'query').mockReturnValue(of(new HttpResponse({ body: reportTemplateCollection })));
      const additionalReportTemplates = [reportTemplate];
      const expectedCollection: IReportTemplate[] = [...additionalReportTemplates, ...reportTemplateCollection];
      jest.spyOn(reportTemplateService, 'addReportTemplateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ xlsxReportRequisition });
      comp.ngOnInit();

      expect(reportTemplateService.query).toHaveBeenCalled();
      expect(reportTemplateService.addReportTemplateToCollectionIfMissing).toHaveBeenCalledWith(
        reportTemplateCollection,
        ...additionalReportTemplates
      );
      expect(comp.reportTemplatesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const xlsxReportRequisition: IXlsxReportRequisition = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 30961 }];
      xlsxReportRequisition.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 47499 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ xlsxReportRequisition });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const xlsxReportRequisition: IXlsxReportRequisition = { id: 456 };
      const reportTemplate: IReportTemplate = { id: 21979 };
      xlsxReportRequisition.reportTemplate = reportTemplate;
      const placeholders: IPlaceholder = { id: 75395 };
      xlsxReportRequisition.placeholders = [placeholders];

      activatedRoute.data = of({ xlsxReportRequisition });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(xlsxReportRequisition));
      expect(comp.reportTemplatesSharedCollection).toContain(reportTemplate);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XlsxReportRequisition>>();
      const xlsxReportRequisition = { id: 123 };
      jest.spyOn(xlsxReportRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xlsxReportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: xlsxReportRequisition }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(xlsxReportRequisitionService.update).toHaveBeenCalledWith(xlsxReportRequisition);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XlsxReportRequisition>>();
      const xlsxReportRequisition = new XlsxReportRequisition();
      jest.spyOn(xlsxReportRequisitionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xlsxReportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: xlsxReportRequisition }));
      saveSubject.complete();

      // THEN
      // expect(xlsxReportRequisitionService.create).toHaveBeenCalledWith(xlsxReportRequisition);
      // expect(comp.isSaving).toEqual(false);
      // TODO expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<XlsxReportRequisition>>();
      const xlsxReportRequisition = { id: 123 };
      jest.spyOn(xlsxReportRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ xlsxReportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(xlsxReportRequisitionService.update).toHaveBeenCalledWith(xlsxReportRequisition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackReportTemplateById', () => {
      it('Should return tracked ReportTemplate primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackReportTemplateById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPlaceholderById', () => {
      it('Should return tracked Placeholder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlaceholderById(0, entity);
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
