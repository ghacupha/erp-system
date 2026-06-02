jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CounterpartyTypeService } from '../service/counterparty-type.service';
import { ICounterpartyType, CounterpartyType } from '../counterparty-type.model';

import { CounterpartyTypeUpdateComponent } from './counterparty-type-update.component';

describe('CounterpartyType Management Update Component', () => {
  let comp: CounterpartyTypeUpdateComponent;
  let fixture: ComponentFixture<CounterpartyTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let counterpartyTypeService: CounterpartyTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CounterpartyTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CounterpartyTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CounterpartyTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    counterpartyTypeService = TestBed.inject(CounterpartyTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const counterpartyType: ICounterpartyType = { id: 456 };

      activatedRoute.data = of({ counterpartyType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(counterpartyType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CounterpartyType>>();
      const counterpartyType = { id: 123 };
      jest.spyOn(counterpartyTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counterpartyType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: counterpartyType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(counterpartyTypeService.update).toHaveBeenCalledWith(counterpartyType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CounterpartyType>>();
      const counterpartyType = new CounterpartyType();
      jest.spyOn(counterpartyTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counterpartyType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: counterpartyType }));
      saveSubject.complete();

      // THEN
      expect(counterpartyTypeService.create).toHaveBeenCalledWith(counterpartyType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CounterpartyType>>();
      const counterpartyType = { id: 123 };
      jest.spyOn(counterpartyTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counterpartyType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(counterpartyTypeService.update).toHaveBeenCalledWith(counterpartyType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
