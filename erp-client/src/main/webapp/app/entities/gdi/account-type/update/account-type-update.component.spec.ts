jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AccountTypeService } from '../service/account-type.service';
import { IAccountType, AccountType } from '../account-type.model';

import { AccountTypeUpdateComponent } from './account-type-update.component';

describe('AccountType Management Update Component', () => {
  let comp: AccountTypeUpdateComponent;
  let fixture: ComponentFixture<AccountTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accountTypeService: AccountTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AccountTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AccountTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accountTypeService = TestBed.inject(AccountTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const accountType: IAccountType = { id: 456 };

      activatedRoute.data = of({ accountType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(accountType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountType>>();
      const accountType = { id: 123 };
      jest.spyOn(accountTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(accountTypeService.update).toHaveBeenCalledWith(accountType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountType>>();
      const accountType = new AccountType();
      jest.spyOn(accountTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountType }));
      saveSubject.complete();

      // THEN
      expect(accountTypeService.create).toHaveBeenCalledWith(accountType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountType>>();
      const accountType = { id: 123 };
      jest.spyOn(accountTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accountTypeService.update).toHaveBeenCalledWith(accountType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
