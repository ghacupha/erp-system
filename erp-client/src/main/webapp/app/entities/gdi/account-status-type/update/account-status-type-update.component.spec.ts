///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AccountStatusTypeService } from '../service/account-status-type.service';
import { IAccountStatusType, AccountStatusType } from '../account-status-type.model';

import { AccountStatusTypeUpdateComponent } from './account-status-type-update.component';

describe('AccountStatusType Management Update Component', () => {
  let comp: AccountStatusTypeUpdateComponent;
  let fixture: ComponentFixture<AccountStatusTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accountStatusTypeService: AccountStatusTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AccountStatusTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AccountStatusTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountStatusTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accountStatusTypeService = TestBed.inject(AccountStatusTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const accountStatusType: IAccountStatusType = { id: 456 };

      activatedRoute.data = of({ accountStatusType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(accountStatusType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountStatusType>>();
      const accountStatusType = { id: 123 };
      jest.spyOn(accountStatusTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountStatusType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountStatusType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(accountStatusTypeService.update).toHaveBeenCalledWith(accountStatusType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountStatusType>>();
      const accountStatusType = new AccountStatusType();
      jest.spyOn(accountStatusTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountStatusType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountStatusType }));
      saveSubject.complete();

      // THEN
      expect(accountStatusTypeService.create).toHaveBeenCalledWith(accountStatusType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountStatusType>>();
      const accountStatusType = { id: 123 };
      jest.spyOn(accountStatusTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountStatusType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accountStatusTypeService.update).toHaveBeenCalledWith(accountStatusType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
