jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FraudTypeService } from '../service/fraud-type.service';
import { IFraudType, FraudType } from '../fraud-type.model';

import { FraudTypeUpdateComponent } from './fraud-type-update.component';

describe('FraudType Management Update Component', () => {
  let comp: FraudTypeUpdateComponent;
  let fixture: ComponentFixture<FraudTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fraudTypeService: FraudTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FraudTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FraudTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FraudTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fraudTypeService = TestBed.inject(FraudTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fraudType: IFraudType = { id: 456 };

      activatedRoute.data = of({ fraudType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(fraudType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FraudType>>();
      const fraudType = { id: 123 };
      jest.spyOn(fraudTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fraudType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fraudType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(fraudTypeService.update).toHaveBeenCalledWith(fraudType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FraudType>>();
      const fraudType = new FraudType();
      jest.spyOn(fraudTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fraudType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fraudType }));
      saveSubject.complete();

      // THEN
      expect(fraudTypeService.create).toHaveBeenCalledWith(fraudType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FraudType>>();
      const fraudType = { id: 123 };
      jest.spyOn(fraudTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fraudType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fraudTypeService.update).toHaveBeenCalledWith(fraudType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
