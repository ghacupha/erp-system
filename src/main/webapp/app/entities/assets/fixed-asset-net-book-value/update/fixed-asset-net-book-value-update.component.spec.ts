jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FixedAssetNetBookValueService } from '../service/fixed-asset-net-book-value.service';
import { IFixedAssetNetBookValue, FixedAssetNetBookValue } from '../fixed-asset-net-book-value.model';

import { FixedAssetNetBookValueUpdateComponent } from './fixed-asset-net-book-value-update.component';

describe('Component Tests', () => {
  describe('FixedAssetNetBookValue Management Update Component', () => {
    let comp: FixedAssetNetBookValueUpdateComponent;
    let fixture: ComponentFixture<FixedAssetNetBookValueUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fixedAssetNetBookValueService: FixedAssetNetBookValueService;

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

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const fixedAssetNetBookValue: IFixedAssetNetBookValue = { id: 456 };

        activatedRoute.data = of({ fixedAssetNetBookValue });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fixedAssetNetBookValue));
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
  });
});
