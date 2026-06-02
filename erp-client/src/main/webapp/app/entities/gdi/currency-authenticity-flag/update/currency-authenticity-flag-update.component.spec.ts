jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CurrencyAuthenticityFlagService } from '../service/currency-authenticity-flag.service';
import { ICurrencyAuthenticityFlag, CurrencyAuthenticityFlag } from '../currency-authenticity-flag.model';

import { CurrencyAuthenticityFlagUpdateComponent } from './currency-authenticity-flag-update.component';

describe('CurrencyAuthenticityFlag Management Update Component', () => {
  let comp: CurrencyAuthenticityFlagUpdateComponent;
  let fixture: ComponentFixture<CurrencyAuthenticityFlagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let currencyAuthenticityFlagService: CurrencyAuthenticityFlagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CurrencyAuthenticityFlagUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CurrencyAuthenticityFlagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CurrencyAuthenticityFlagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    currencyAuthenticityFlagService = TestBed.inject(CurrencyAuthenticityFlagService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const currencyAuthenticityFlag: ICurrencyAuthenticityFlag = { id: 456 };

      activatedRoute.data = of({ currencyAuthenticityFlag });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(currencyAuthenticityFlag));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CurrencyAuthenticityFlag>>();
      const currencyAuthenticityFlag = { id: 123 };
      jest.spyOn(currencyAuthenticityFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ currencyAuthenticityFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: currencyAuthenticityFlag }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(currencyAuthenticityFlagService.update).toHaveBeenCalledWith(currencyAuthenticityFlag);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CurrencyAuthenticityFlag>>();
      const currencyAuthenticityFlag = new CurrencyAuthenticityFlag();
      jest.spyOn(currencyAuthenticityFlagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ currencyAuthenticityFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: currencyAuthenticityFlag }));
      saveSubject.complete();

      // THEN
      expect(currencyAuthenticityFlagService.create).toHaveBeenCalledWith(currencyAuthenticityFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CurrencyAuthenticityFlag>>();
      const currencyAuthenticityFlag = { id: 123 };
      jest.spyOn(currencyAuthenticityFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ currencyAuthenticityFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(currencyAuthenticityFlagService.update).toHaveBeenCalledWith(currencyAuthenticityFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
