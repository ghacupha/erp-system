jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CounterPartyDealTypeService } from '../service/counter-party-deal-type.service';
import { ICounterPartyDealType, CounterPartyDealType } from '../counter-party-deal-type.model';

import { CounterPartyDealTypeUpdateComponent } from './counter-party-deal-type-update.component';

describe('CounterPartyDealType Management Update Component', () => {
  let comp: CounterPartyDealTypeUpdateComponent;
  let fixture: ComponentFixture<CounterPartyDealTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let counterPartyDealTypeService: CounterPartyDealTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CounterPartyDealTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CounterPartyDealTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CounterPartyDealTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    counterPartyDealTypeService = TestBed.inject(CounterPartyDealTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const counterPartyDealType: ICounterPartyDealType = { id: 456 };

      activatedRoute.data = of({ counterPartyDealType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(counterPartyDealType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CounterPartyDealType>>();
      const counterPartyDealType = { id: 123 };
      jest.spyOn(counterPartyDealTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counterPartyDealType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: counterPartyDealType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(counterPartyDealTypeService.update).toHaveBeenCalledWith(counterPartyDealType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CounterPartyDealType>>();
      const counterPartyDealType = new CounterPartyDealType();
      jest.spyOn(counterPartyDealTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counterPartyDealType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: counterPartyDealType }));
      saveSubject.complete();

      // THEN
      expect(counterPartyDealTypeService.create).toHaveBeenCalledWith(counterPartyDealType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CounterPartyDealType>>();
      const counterPartyDealType = { id: 123 };
      jest.spyOn(counterPartyDealTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counterPartyDealType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(counterPartyDealTypeService.update).toHaveBeenCalledWith(counterPartyDealType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
