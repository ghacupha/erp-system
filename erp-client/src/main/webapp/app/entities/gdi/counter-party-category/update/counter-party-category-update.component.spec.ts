jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CounterPartyCategoryService } from '../service/counter-party-category.service';
import { ICounterPartyCategory, CounterPartyCategory } from '../counter-party-category.model';

import { CounterPartyCategoryUpdateComponent } from './counter-party-category-update.component';

describe('CounterPartyCategory Management Update Component', () => {
  let comp: CounterPartyCategoryUpdateComponent;
  let fixture: ComponentFixture<CounterPartyCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let counterPartyCategoryService: CounterPartyCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CounterPartyCategoryUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CounterPartyCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CounterPartyCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    counterPartyCategoryService = TestBed.inject(CounterPartyCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const counterPartyCategory: ICounterPartyCategory = { id: 456 };

      activatedRoute.data = of({ counterPartyCategory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(counterPartyCategory));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CounterPartyCategory>>();
      const counterPartyCategory = { id: 123 };
      jest.spyOn(counterPartyCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counterPartyCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: counterPartyCategory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(counterPartyCategoryService.update).toHaveBeenCalledWith(counterPartyCategory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CounterPartyCategory>>();
      const counterPartyCategory = new CounterPartyCategory();
      jest.spyOn(counterPartyCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counterPartyCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: counterPartyCategory }));
      saveSubject.complete();

      // THEN
      expect(counterPartyCategoryService.create).toHaveBeenCalledWith(counterPartyCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CounterPartyCategory>>();
      const counterPartyCategory = { id: 123 };
      jest.spyOn(counterPartyCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ counterPartyCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(counterPartyCategoryService.update).toHaveBeenCalledWith(counterPartyCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
