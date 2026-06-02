jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { IsoCurrencyCodeService } from '../service/iso-currency-code.service';
import { IIsoCurrencyCode, IsoCurrencyCode } from '../iso-currency-code.model';

import { IsoCurrencyCodeUpdateComponent } from './iso-currency-code-update.component';

describe('IsoCurrencyCode Management Update Component', () => {
  let comp: IsoCurrencyCodeUpdateComponent;
  let fixture: ComponentFixture<IsoCurrencyCodeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let isoCurrencyCodeService: IsoCurrencyCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [IsoCurrencyCodeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(IsoCurrencyCodeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IsoCurrencyCodeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    isoCurrencyCodeService = TestBed.inject(IsoCurrencyCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const isoCurrencyCode: IIsoCurrencyCode = { id: 456 };

      activatedRoute.data = of({ isoCurrencyCode });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(isoCurrencyCode));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IsoCurrencyCode>>();
      const isoCurrencyCode = { id: 123 };
      jest.spyOn(isoCurrencyCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ isoCurrencyCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: isoCurrencyCode }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(isoCurrencyCodeService.update).toHaveBeenCalledWith(isoCurrencyCode);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IsoCurrencyCode>>();
      const isoCurrencyCode = new IsoCurrencyCode();
      jest.spyOn(isoCurrencyCodeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ isoCurrencyCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: isoCurrencyCode }));
      saveSubject.complete();

      // THEN
      expect(isoCurrencyCodeService.create).toHaveBeenCalledWith(isoCurrencyCode);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IsoCurrencyCode>>();
      const isoCurrencyCode = { id: 123 };
      jest.spyOn(isoCurrencyCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ isoCurrencyCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(isoCurrencyCodeService.update).toHaveBeenCalledWith(isoCurrencyCode);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
