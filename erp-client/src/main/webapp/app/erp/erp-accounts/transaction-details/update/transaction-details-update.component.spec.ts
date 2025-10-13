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

import { IPlaceholder } from '../../../erp-pages/placeholder/placeholder.model';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TransactionDetailsService } from '../service/transaction-details.service';
import { ITransactionDetails, TransactionDetails } from '../transaction-details.model';

import { TransactionDetailsUpdateComponent } from './transaction-details-update.component';
import { ITransactionAccount } from '../../transaction-account/transaction-account.model';
import { IApplicationUser } from '../../../erp-pages/application-user/application-user.model';
import { ApplicationUserService } from '../../../erp-pages/application-user/service/application-user.service';
import { TransactionAccountService } from '../../transaction-account/service/transaction-account.service';
import { PlaceholderService } from '../../../erp-pages/placeholder/service/placeholder.service';

describe('TransactionDetails Management Update Component', () => {
  let comp: TransactionDetailsUpdateComponent;
  let fixture: ComponentFixture<TransactionDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transactionDetailsService: TransactionDetailsService;
  let transactionAccountService: TransactionAccountService;
  let placeholderService: PlaceholderService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TransactionDetailsUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TransactionDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransactionDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transactionDetailsService = TestBed.inject(TransactionDetailsService);
    transactionAccountService = TestBed.inject(TransactionAccountService);
    placeholderService = TestBed.inject(PlaceholderService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TransactionAccount query and add missing value', () => {
      const transactionDetails: ITransactionDetails = { id: 456 };
      const debitAccount: ITransactionAccount = { id: 33302 };
      transactionDetails.debitAccount = debitAccount;
      const creditAccount: ITransactionAccount = { id: 28488 };
      transactionDetails.creditAccount = creditAccount;

      const transactionAccountCollection: ITransactionAccount[] = [{ id: 2124 }];
      jest.spyOn(transactionAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionAccountCollection })));
      const additionalTransactionAccounts = [debitAccount, creditAccount];
      const expectedCollection: ITransactionAccount[] = [...additionalTransactionAccounts, ...transactionAccountCollection];
      jest.spyOn(transactionAccountService, 'addTransactionAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionDetails });
      comp.ngOnInit();

      expect(transactionAccountService.query).toHaveBeenCalled();
      expect(transactionAccountService.addTransactionAccountToCollectionIfMissing).toHaveBeenCalledWith(
        transactionAccountCollection,
        ...additionalTransactionAccounts
      );
      expect(comp.transactionAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Placeholder query and add missing value', () => {
      const transactionDetails: ITransactionDetails = { id: 456 };
      const placeholders: IPlaceholder[] = [{ id: 17829 }];
      transactionDetails.placeholders = placeholders;

      const placeholderCollection: IPlaceholder[] = [{ id: 74872 }];
      jest.spyOn(placeholderService, 'query').mockReturnValue(of(new HttpResponse({ body: placeholderCollection })));
      const additionalPlaceholders = [...placeholders];
      const expectedCollection: IPlaceholder[] = [...additionalPlaceholders, ...placeholderCollection];
      jest.spyOn(placeholderService, 'addPlaceholderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionDetails });
      comp.ngOnInit();

      expect(placeholderService.query).toHaveBeenCalled();
      expect(placeholderService.addPlaceholderToCollectionIfMissing).toHaveBeenCalledWith(placeholderCollection, ...additionalPlaceholders);
      expect(comp.placeholdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ApplicationUser query and add missing value', () => {
      const transactionDetails: ITransactionDetails = { id: 456 };
      const postedBy: IApplicationUser = { id: 90290 };
      transactionDetails.postedBy = postedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 20070 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [postedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transactionDetails });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const transactionDetails: ITransactionDetails = { id: 456 };
      const debitAccount: ITransactionAccount = { id: 59075 };
      transactionDetails.debitAccount = debitAccount;
      const creditAccount: ITransactionAccount = { id: 86692 };
      transactionDetails.creditAccount = creditAccount;
      const placeholders: IPlaceholder = { id: 18799 };
      transactionDetails.placeholders = [placeholders];
      const postedBy: IApplicationUser = { id: 62637 };
      transactionDetails.postedBy = postedBy;

      activatedRoute.data = of({ transactionDetails });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(transactionDetails));
      expect(comp.transactionAccountsSharedCollection).toContain(debitAccount);
      expect(comp.transactionAccountsSharedCollection).toContain(creditAccount);
      expect(comp.placeholdersSharedCollection).toContain(placeholders);
      expect(comp.applicationUsersSharedCollection).toContain(postedBy);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TransactionDetails>>();
      const transactionDetails = { id: 123 };
      jest.spyOn(transactionDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionDetails }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(transactionDetailsService.update).toHaveBeenCalledWith(transactionDetails);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TransactionDetails>>();
      const transactionDetails = new TransactionDetails();
      jest.spyOn(transactionDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transactionDetails }));
      saveSubject.complete();

      // THEN
      expect(transactionDetailsService.create).toHaveBeenCalledWith(transactionDetails);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TransactionDetails>>();
      const transactionDetails = { id: 123 };
      jest.spyOn(transactionDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transactionDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transactionDetailsService.update).toHaveBeenCalledWith(transactionDetails);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTransactionAccountById', () => {
      it('Should return tracked TransactionAccount primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTransactionAccountById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPlaceholderById', () => {
      it('Should return tracked Placeholder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlaceholderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackApplicationUserById', () => {
      it('Should return tracked ApplicationUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApplicationUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedPlaceholder', () => {
      it('Should return option if no Placeholder is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPlaceholder(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Placeholder for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPlaceholder(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Placeholder is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPlaceholder(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
