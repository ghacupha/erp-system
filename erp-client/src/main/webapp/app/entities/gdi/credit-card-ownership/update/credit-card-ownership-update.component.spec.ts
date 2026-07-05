jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CreditCardOwnershipService } from '../service/credit-card-ownership.service';
import { ICreditCardOwnership, CreditCardOwnership } from '../credit-card-ownership.model';

import { CreditCardOwnershipUpdateComponent } from './credit-card-ownership-update.component';

describe('CreditCardOwnership Management Update Component', () => {
  let comp: CreditCardOwnershipUpdateComponent;
  let fixture: ComponentFixture<CreditCardOwnershipUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let creditCardOwnershipService: CreditCardOwnershipService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CreditCardOwnershipUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CreditCardOwnershipUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CreditCardOwnershipUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    creditCardOwnershipService = TestBed.inject(CreditCardOwnershipService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const creditCardOwnership: ICreditCardOwnership = { id: 456 };

      activatedRoute.data = of({ creditCardOwnership });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(creditCardOwnership));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CreditCardOwnership>>();
      const creditCardOwnership = { id: 123 };
      jest.spyOn(creditCardOwnershipService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ creditCardOwnership });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: creditCardOwnership }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(creditCardOwnershipService.update).toHaveBeenCalledWith(creditCardOwnership);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CreditCardOwnership>>();
      const creditCardOwnership = new CreditCardOwnership();
      jest.spyOn(creditCardOwnershipService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ creditCardOwnership });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: creditCardOwnership }));
      saveSubject.complete();

      // THEN
      expect(creditCardOwnershipService.create).toHaveBeenCalledWith(creditCardOwnership);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CreditCardOwnership>>();
      const creditCardOwnership = { id: 123 };
      jest.spyOn(creditCardOwnershipService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ creditCardOwnership });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(creditCardOwnershipService.update).toHaveBeenCalledWith(creditCardOwnership);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
