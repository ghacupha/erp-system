jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FixedAssetDepreciationService } from '../service/fixed-asset-depreciation.service';
import { IFixedAssetDepreciation, FixedAssetDepreciation } from '../fixed-asset-depreciation.model';

import { FixedAssetDepreciationUpdateComponent } from './fixed-asset-depreciation-update.component';

describe('Component Tests', () => {
  describe('FixedAssetDepreciation Management Update Component', () => {
    let comp: FixedAssetDepreciationUpdateComponent;
    let fixture: ComponentFixture<FixedAssetDepreciationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fixedAssetDepreciationService: FixedAssetDepreciationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FixedAssetDepreciationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FixedAssetDepreciationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FixedAssetDepreciationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fixedAssetDepreciationService = TestBed.inject(FixedAssetDepreciationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const fixedAssetDepreciation: IFixedAssetDepreciation = { id: 456 };

        activatedRoute.data = of({ fixedAssetDepreciation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fixedAssetDepreciation));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FixedAssetDepreciation>>();
        const fixedAssetDepreciation = { id: 123 };
        jest.spyOn(fixedAssetDepreciationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fixedAssetDepreciation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fixedAssetDepreciation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fixedAssetDepreciationService.update).toHaveBeenCalledWith(fixedAssetDepreciation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FixedAssetDepreciation>>();
        const fixedAssetDepreciation = new FixedAssetDepreciation();
        jest.spyOn(fixedAssetDepreciationService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fixedAssetDepreciation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fixedAssetDepreciation }));
        saveSubject.complete();

        // THEN
        expect(fixedAssetDepreciationService.create).toHaveBeenCalledWith(fixedAssetDepreciation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FixedAssetDepreciation>>();
        const fixedAssetDepreciation = { id: 123 };
        jest.spyOn(fixedAssetDepreciationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fixedAssetDepreciation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fixedAssetDepreciationService.update).toHaveBeenCalledWith(fixedAssetDepreciation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
