jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FixedAssetNetBookValueService } from '../service/fixed-asset-net-book-value.service';
import { IFixedAssetNetBookValue, FixedAssetNetBookValue } from '../fixed-asset-net-book-value.model';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { PlaceholderService } from 'app/entities/erpService/placeholder/service/placeholder.service';

import { FixedAssetNetBookValueUpdateComponent } from './fixed-asset-net-book-value-update.component';

describe('FixedAssetNetBookValue Management Update Component', () => {
  let comp: FixedAssetNetBookValueUpdateComponent;
  let fixture: ComponentFixture<FixedAssetNetBookValueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fixedAssetNetBookValueService: FixedAssetNetBookValueService;
  let placeholderService: PlaceholderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FixedAssetNetBookValueUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FixedAssetNetBookValueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FixedAssetNetBookValueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fixedAssetNetBookValueService = TestBed.inject(FixedAssetNetBookValueService);
    placeholderService = TestBed.inject(PlaceholderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Placeholder query and add missing value', () => {
      const fixedAssetNetBookValue: IFixedAssetNetBookValue = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 24399 }];
      fixedAssetNetBookValue.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 35519 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fixedAssetNetBookValue });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const fixedAssetNetBookValue: IFixedAssetNetBookValue = { id: 456 };
      const placeholders: IPlaceholder = { id: 23707 };
      fixedAssetNetBookValue.placeholders = [placeholders];

      activatedRoute.data = of({ fixedAssetNetBookValue });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(fixedAssetNetBookValue));
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FixedAssetNetBookValue>>();
      const fixedAssetNetBookValue = { id: 123 };
      jest.spyOn(fixedAssetNetBookValueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fixedAssetNetBookValue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fixedAssetNetBookValue }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(fixedAssetNetBookValueService.update).toHaveBeenCalledWith(fixedAssetNetBookValue);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FixedAssetNetBookValue>>();
      const fixedAssetNetBookValue = new FixedAssetNetBookValue();
      jest.spyOn(fixedAssetNetBookValueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fixedAssetNetBookValue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fixedAssetNetBookValue }));
      saveSubject.complete();

      // THEN
      expect(fixedAssetNetBookValueService.create).toHaveBeenCalledWith(fixedAssetNetBookValue);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FixedAssetNetBookValue>>();
      const fixedAssetNetBookValue = { id: 123 };
      jest.spyOn(fixedAssetNetBookValueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fixedAssetNetBookValue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fixedAssetNetBookValueService.update).toHaveBeenCalledWith(fixedAssetNetBookValue);
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
