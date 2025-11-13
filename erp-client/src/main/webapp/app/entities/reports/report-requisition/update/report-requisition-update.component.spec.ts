///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import { ReportRequisitionService } from '../service/report-requisition.service';
import { IReportRequisition, ReportRequisition } from '../report-requisition.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/system/placeholder/service/placeholder.service';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { UniversallyUniqueMappingService } from 'app/entities/system/universally-unique-mapping/service/universally-unique-mapping.service';
import { IReportTemplate } from 'app/entities/reports/report-template/report-template.model';
import { ReportTemplateService } from 'app/entities/reports/report-template/service/report-template.service';
import { IReportContentType } from 'app/entities/reports/report-content-type/report-content-type.model';
import { ReportContentTypeService } from 'app/entities/reports/report-content-type/service/report-content-type.service';

import { ReportRequisitionUpdateComponent } from './report-requisition-update.component';

describe('ReportRequisition Management Update Component', () => {
  let comp: ReportRequisitionUpdateComponent;
  let fixture: ComponentFixture<ReportRequisitionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportRequisitionService: ReportRequisitionService;
  let placeholderService: PlaceholderService;
  let universallyUniqueMappingService: UniversallyUniqueMappingService;
  let reportTemplateService: ReportTemplateService;
  let reportContentTypeService: ReportContentTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ReportRequisitionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ReportRequisitionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportRequisitionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportRequisitionService = TestBed.inject(ReportRequisitionService);
    placeholderService = TestBed.inject(PlaceholderService);
    universallyUniqueMappingService = TestBed.inject(UniversallyUniqueMappingService);
    reportTemplateService = TestBed.inject(ReportTemplateService);
    reportContentTypeService = TestBed.inject(ReportContentTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const reportRequisition: IReportRequisition = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 52719 }];
      reportRequisition.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 54364 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportRequisition });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UniversallyUniqueMapping query and add missing value', () => {
      const reportRequisition: IReportRequisition = { id: 456 };
      const parameters: IUniversallyUniqueMapping[] = [{ id: 57316 }];
      reportRequisition.parameters = parameters;

      const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 26345 }];
      jest
        .spyOn(universallyUniqueMappingService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: universallyUniqueMappingCollection })));
      const additionalUniversallyUniqueMappings = [...parameters];
      const expectedCollection: IUniversallyUniqueMapping[] = [
        ...additionalUniversallyUniqueMappings,
        ...universallyUniqueMappingCollection,
      ];
      jest.spyOn(universallyUniqueMappingService, 'addUniversallyUniqueMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportRequisition });
      comp.ngOnInit();

      expect(universallyUniqueMappingService.query).toHaveBeenCalled();
      expect(universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing).toHaveBeenCalledWith(
        universallyUniqueMappingCollection,
        ...additionalUniversallyUniqueMappings
      );
      expect(comp.universallyUniqueMappingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ReportTemplate query and add missing value', () => {
      const reportRequisition: IReportRequisition = { id: 456 };
      const reportTemplate: IReportTemplate = { id: 98779 };
      reportRequisition.reportTemplate = reportTemplate;

      const reportTemplateCollection: IReportTemplate[] = [{ id: 77360 }];
      jest.spyOn(reportTemplateService, 'query').mockReturnValue(of(new HttpResponse({ body: reportTemplateCollection })));
      const additionalReportTemplates = [reportTemplate];
      const expectedCollection: IReportTemplate[] = [...additionalReportTemplates, ...reportTemplateCollection];
      jest.spyOn(reportTemplateService, 'addReportTemplateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportRequisition });
      comp.ngOnInit();

      expect(reportTemplateService.query).toHaveBeenCalled();
      expect(reportTemplateService.addReportTemplateToCollectionIfMissing).toHaveBeenCalledWith(
        reportTemplateCollection,
        ...additionalReportTemplates
      );
      expect(comp.reportTemplatesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ReportContentType query and add missing value', () => {
      const reportRequisition: IReportRequisition = { id: 456 };
      const reportContentType: IReportContentType = { id: 48187 };
      reportRequisition.reportContentType = reportContentType;

      const reportContentTypeCollection: IReportContentType[] = [{ id: 4986 }];
      jest.spyOn(reportContentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: reportContentTypeCollection })));
      const additionalReportContentTypes = [reportContentType];
      const expectedCollection: IReportContentType[] = [...additionalReportContentTypes, ...reportContentTypeCollection];
      jest.spyOn(reportContentTypeService, 'addReportContentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportRequisition });
      comp.ngOnInit();

      expect(reportContentTypeService.query).toHaveBeenCalled();
      expect(reportContentTypeService.addReportContentTypeToCollectionIfMissing).toHaveBeenCalledWith(
        reportContentTypeCollection,
        ...additionalReportContentTypes
      );
      expect(comp.reportContentTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reportRequisition: IReportRequisition = { id: 456 };
      const placeholders: IPlaceholder = { id: 33255 };
      reportRequisition.placeholders = [placeholders];
      const parameters: IUniversallyUniqueMapping = { id: 99069 };
      reportRequisition.parameters = [parameters];
      const reportTemplate: IReportTemplate = { id: 5925 };
      reportRequisition.reportTemplate = reportTemplate;
      const reportContentType: IReportContentType = { id: 29644 };
      reportRequisition.reportContentType = reportContentType;

      activatedRoute.data = of({ reportRequisition });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(reportRequisition));
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.universallyUniqueMappingsSharedCollection).toContain(parameters);
      expect(comp.reportTemplatesSharedCollection).toContain(reportTemplate);
      expect(comp.reportContentTypesSharedCollection).toContain(reportContentType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReportRequisition>>();
      const reportRequisition = { id: 123 };
      jest.spyOn(reportRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportRequisition }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportRequisitionService.update).toHaveBeenCalledWith(reportRequisition);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReportRequisition>>();
      const reportRequisition = new ReportRequisition();
      jest.spyOn(reportRequisitionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportRequisition }));
      saveSubject.complete();

      // THEN
      expect(reportRequisitionService.create).toHaveBeenCalledWith(reportRequisition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReportRequisition>>();
      const reportRequisition = { id: 123 };
      jest.spyOn(reportRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportRequisitionService.update).toHaveBeenCalledWith(reportRequisition);
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

    describe('trackUniversallyUniqueMappingById', () => {
      it('Should return tracked UniversallyUniqueMapping primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUniversallyUniqueMappingById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackReportTemplateById', () => {
      it('Should return tracked ReportTemplate primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackReportTemplateById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackReportContentTypeById', () => {
      it('Should return tracked ReportContentType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackReportContentTypeById(0, entity);
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

    describe('getSelectedUniversallyUniqueMapping', () => {
      it('Should return option if no UniversallyUniqueMapping is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedUniversallyUniqueMapping(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected UniversallyUniqueMapping for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this UniversallyUniqueMapping is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
