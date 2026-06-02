jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FxRateTypeService } from '../service/fx-rate-type.service';
import { IFxRateType, FxRateType } from '../fx-rate-type.model';

import { FxRateTypeUpdateComponent } from './fx-rate-type-update.component';

describe('FxRateType Management Update Component', () => {
  let comp: FxRateTypeUpdateComponent;
  let fixture: ComponentFixture<FxRateTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fxRateTypeService: FxRateTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FxRateTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FxRateTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FxRateTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fxRateTypeService = TestBed.inject(FxRateTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fxRateType: IFxRateType = { id: 456 };

      activatedRoute.data = of({ fxRateType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(fxRateType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxRateType>>();
      const fxRateType = { id: 123 };
      jest.spyOn(fxRateTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxRateType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fxRateType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(fxRateTypeService.update).toHaveBeenCalledWith(fxRateType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxRateType>>();
      const fxRateType = new FxRateType();
      jest.spyOn(fxRateTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxRateType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fxRateType }));
      saveSubject.complete();

      // THEN
      expect(fxRateTypeService.create).toHaveBeenCalledWith(fxRateType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxRateType>>();
      const fxRateType = { id: 123 };
      jest.spyOn(fxRateTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxRateType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fxRateTypeService.update).toHaveBeenCalledWith(fxRateType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
