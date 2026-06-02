jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReasonsForBouncedChequeService } from '../service/reasons-for-bounced-cheque.service';
import { IReasonsForBouncedCheque, ReasonsForBouncedCheque } from '../reasons-for-bounced-cheque.model';

import { ReasonsForBouncedChequeUpdateComponent } from './reasons-for-bounced-cheque-update.component';

describe('ReasonsForBouncedCheque Management Update Component', () => {
  let comp: ReasonsForBouncedChequeUpdateComponent;
  let fixture: ComponentFixture<ReasonsForBouncedChequeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reasonsForBouncedChequeService: ReasonsForBouncedChequeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ReasonsForBouncedChequeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ReasonsForBouncedChequeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReasonsForBouncedChequeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reasonsForBouncedChequeService = TestBed.inject(ReasonsForBouncedChequeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const reasonsForBouncedCheque: IReasonsForBouncedCheque = { id: 456 };

      activatedRoute.data = of({ reasonsForBouncedCheque });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(reasonsForBouncedCheque));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReasonsForBouncedCheque>>();
      const reasonsForBouncedCheque = { id: 123 };
      jest.spyOn(reasonsForBouncedChequeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reasonsForBouncedCheque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reasonsForBouncedCheque }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(reasonsForBouncedChequeService.update).toHaveBeenCalledWith(reasonsForBouncedCheque);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReasonsForBouncedCheque>>();
      const reasonsForBouncedCheque = new ReasonsForBouncedCheque();
      jest.spyOn(reasonsForBouncedChequeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reasonsForBouncedCheque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reasonsForBouncedCheque }));
      saveSubject.complete();

      // THEN
      expect(reasonsForBouncedChequeService.create).toHaveBeenCalledWith(reasonsForBouncedCheque);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReasonsForBouncedCheque>>();
      const reasonsForBouncedCheque = { id: 123 };
      jest.spyOn(reasonsForBouncedChequeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reasonsForBouncedCheque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reasonsForBouncedChequeService.update).toHaveBeenCalledWith(reasonsForBouncedCheque);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
