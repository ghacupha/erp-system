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

import { DepreciationMethodService } from '../../depreciation-method/service/depreciation-method.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AssetCategoryService } from '../service/asset-category.service';
import { IAssetCategory, AssetCategory } from '../asset-category.model';

import { AssetCategoryUpdateComponent } from './asset-category-update.component';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';
import { IDepreciationMethod } from '../../depreciation-method/depreciation-method.model';
import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';
import { ErpCommonModule } from '../../../erp-common/erp-common.module';

describe('AssetCategory Management Update Component', () => {
  let comp: AssetCategoryUpdateComponent;
  let fixture: ComponentFixture<AssetCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetCategoryService: AssetCategoryService;
  let depreciationMethodService: DepreciationMethodService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ErpCommonModule, HttpClientTestingModule],
      declarations: [AssetCategoryUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AssetCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetCategoryService = TestBed.inject(AssetCategoryService);
    depreciationMethodService = TestBed.inject(DepreciationMethodService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call DepreciationMethod query and add missing value', () => {
      const assetCategory: IAssetCategory = { id: 456 };
      const depreciationMethod: IDepreciationMethod = { id: 57131 };
      assetCategory.depreciationMethod = depreciationMethod;

      const depreciationMethodCollection: IDepreciationMethod[] = [{ id: 65527 }];
      jest.spyOn(depreciationMethodService, 'query').mockReturnValue(of(new HttpResponse({ body: depreciationMethodCollection })));
      const additionalDepreciationMethods = [depreciationMethod];
      const expectedCollection: IDepreciationMethod[] = [...additionalDepreciationMethods, ...depreciationMethodCollection];
      jest.spyOn(depreciationMethodService, 'addDepreciationMethodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetCategory });
      comp.ngOnInit();

      expect(depreciationMethodService.query).toHaveBeenCalled();
      expect(depreciationMethodService.addDepreciationMethodToCollectionIfMissing).toHaveBeenCalledWith(
        depreciationMethodCollection,
        ...additionalDepreciationMethods
      );
      expect(comp.depreciationMethodsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const assetCategory: IAssetCategory = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 97144 }];
      assetCategory.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 72891 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetCategory });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const assetCategory: IAssetCategory = { id: 456 };
      const depreciationMethod: IDepreciationMethod = { id: 62074 };
      assetCategory.depreciationMethod = depreciationMethod;
      const placeholders: IPlaceholder = { id: 14808 };
      assetCategory.placeholders = [placeholders];

      activatedRoute.data = of({ assetCategory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(assetCategory));
      expect(comp.depreciationMethodsSharedCollection).toContain(depreciationMethod);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetCategory>>();
      const assetCategory = { id: 123 };
      jest.spyOn(assetCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetCategory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetCategoryService.update).toHaveBeenCalledWith(assetCategory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetCategory>>();
      const assetCategory = new AssetCategory();
      jest.spyOn(assetCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetCategory }));
      saveSubject.complete();

      // THEN
      expect(assetCategoryService.create).toHaveBeenCalledWith(assetCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetCategory>>();
      const assetCategory = { id: 123 };
      jest.spyOn(assetCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetCategoryService.update).toHaveBeenCalledWith(assetCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDepreciationMethodById', () => {
      it('Should return tracked DepreciationMethod primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepreciationMethodById(0, entity);
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
