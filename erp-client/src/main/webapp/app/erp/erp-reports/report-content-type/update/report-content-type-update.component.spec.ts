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

import { ISystemContentType } from '../../system-content-type/system-content-type.model';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReportContentTypeService } from '../service/report-content-type.service';
import { IReportContentType, ReportContentType } from '../report-content-type.model';

import { ReportContentTypeUpdateComponent } from './report-content-type-update.component';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { SystemContentTypeService } from '../../system-content-type/service/system-content-type.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';

describe('ReportContentType Management Update Component', () => {
  let comp: ReportContentTypeUpdateComponent;
  let fixture: ComponentFixture<ReportContentTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportContentTypeService: ReportContentTypeService;
  let systemContentTypeService: SystemContentTypeService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ReportContentTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ReportContentTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportContentTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportContentTypeService = TestBed.inject(ReportContentTypeService);
    systemContentTypeService = TestBed.inject(SystemContentTypeService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SystemContentType query and add missing value', () => {
      const reportContentType: IReportContentType = { id: 456 };
      const systemContentType: ISystemContentType = { id: 1952 };
      reportContentType.systemContentType = systemContentType;

      const systemContentTypeCollection: ISystemContentType[] = [{ id: 53175 }];
      jest.spyOn(systemContentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: systemContentTypeCollection })));
      const additionalSystemContentTypes = [systemContentType];
      const expectedCollection: ISystemContentType[] = [...additionalSystemContentTypes, ...systemContentTypeCollection];
      jest.spyOn(systemContentTypeService, 'addSystemContentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportContentType });
      comp.ngOnInit();

      expect(systemContentTypeService.query).toHaveBeenCalled();
      expect(systemContentTypeService.addSystemContentTypeToCollectionIfMissing).toHaveBeenCalledWith(
        systemContentTypeCollection,
        ...additionalSystemContentTypes
      );
      expect(comp.systemContentTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const reportContentType: IReportContentType = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 86234 }];
      reportContentType.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 53639 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportContentType });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reportContentType: IReportContentType = { id: 456 };
      const systemContentType: ISystemContentType = { id: 39763 };
      reportContentType.systemContentType = systemContentType;
      const placeholders: IPlaceholder = { id: 70487 };
      reportContentType.placeholders = [placeholders];

      activatedRoute.data = of({ reportContentType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(reportContentType));
      expect(comp.systemContentTypesSharedCollection).toContain(systemContentType);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReportContentType>>();
      const reportContentType = { id: 123 };
      jest.spyOn(reportContentTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportContentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportContentType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportContentTypeService.update).toHaveBeenCalledWith(reportContentType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReportContentType>>();
      const reportContentType = new ReportContentType();
      jest.spyOn(reportContentTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportContentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportContentType }));
      saveSubject.complete();

      // THEN
      expect(reportContentTypeService.create).toHaveBeenCalledWith(reportContentType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReportContentType>>();
      const reportContentType = { id: 123 };
      jest.spyOn(reportContentTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportContentType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportContentTypeService.update).toHaveBeenCalledWith(reportContentType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSystemContentTypeById', () => {
      it('Should return tracked SystemContentType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSystemContentTypeById(0, entity);
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
