jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FxTransactionRateTypeService } from '../service/fx-transaction-rate-type.service';
import { IFxTransactionRateType, FxTransactionRateType } from '../fx-transaction-rate-type.model';

import { FxTransactionRateTypeUpdateComponent } from './fx-transaction-rate-type-update.component';

describe('FxTransactionRateType Management Update Component', () => {
  let comp: FxTransactionRateTypeUpdateComponent;
  let fixture: ComponentFixture<FxTransactionRateTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fxTransactionRateTypeService: FxTransactionRateTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FxTransactionRateTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FxTransactionRateTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FxTransactionRateTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fxTransactionRateTypeService = TestBed.inject(FxTransactionRateTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fxTransactionRateType: IFxTransactionRateType = { id: 456 };

      activatedRoute.data = of({ fxTransactionRateType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(fxTransactionRateType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxTransactionRateType>>();
      const fxTransactionRateType = { id: 123 };
      jest.spyOn(fxTransactionRateTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxTransactionRateType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fxTransactionRateType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(fxTransactionRateTypeService.update).toHaveBeenCalledWith(fxTransactionRateType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxTransactionRateType>>();
      const fxTransactionRateType = new FxTransactionRateType();
      jest.spyOn(fxTransactionRateTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxTransactionRateType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fxTransactionRateType }));
      saveSubject.complete();

      // THEN
      expect(fxTransactionRateTypeService.create).toHaveBeenCalledWith(fxTransactionRateType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxTransactionRateType>>();
      const fxTransactionRateType = { id: 123 };
      jest.spyOn(fxTransactionRateTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxTransactionRateType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fxTransactionRateTypeService.update).toHaveBeenCalledWith(fxTransactionRateType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
