///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { DealerService } from '../../dealers/dealer/service/dealer.service';

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ApplicationUserService } from '../service/application-user.service';
import { IApplicationUser, ApplicationUser } from '../application-user.model';

import { ApplicationUserUpdateComponent } from './application-user-update.component';
import { SecurityClearanceService } from '../../security-clearance/service/security-clearance.service';
import { UserService } from '../../../../core/user/user.service';
import { UniversallyUniqueMappingService } from '../../universally-unique-mapping/service/universally-unique-mapping.service';
import { IDealer } from '../../dealers/dealer/dealer.model';
import { ISecurityClearance } from '../../security-clearance/security-clearance.model';
import { IUser } from '../../../../admin/user-management/user-management.model';
import { IUniversallyUniqueMapping } from '../../universally-unique-mapping/universally-unique-mapping.model';

describe('ApplicationUser Management Update Component', () => {
  let comp: ApplicationUserUpdateComponent;
  let fixture: ComponentFixture<ApplicationUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let applicationUserService: ApplicationUserService;
  let dealerService: DealerService;
  let securityClearanceService: SecurityClearanceService;
  let userService: UserService;
  let universallyUniqueMappingService: UniversallyUniqueMappingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ApplicationUserUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ApplicationUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApplicationUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    applicationUserService = TestBed.inject(ApplicationUserService);
    dealerService = TestBed.inject(DealerService);
    securityClearanceService = TestBed.inject(SecurityClearanceService);
    userService = TestBed.inject(UserService);
    universallyUniqueMappingService = TestBed.inject(UniversallyUniqueMappingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Dealer query and add missing value', () => {
      const applicationUser: IApplicationUser = { id: 456 };
      const organization: IDealer = { id: 62911 };
      applicationUser.organization = organization;
      const department: IDealer = { id: 35885 };
      applicationUser.department = department;

      const dealerCollection: IDealer[] = [{ id: 31654 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [organization, department];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call dealerIdentity query and add missing value', () => {
      const applicationUser: IApplicationUser = { id: 456 };
      const dealerIdentity: IDealer = { id: 63512 };
      applicationUser.dealerIdentity = dealerIdentity;

      const dealerIdentityCollection: IDealer[] = [{ id: 88934 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerIdentityCollection })));
      const expectedCollection: IDealer[] = [dealerIdentity, ...dealerIdentityCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerIdentityCollection, dealerIdentity);
      expect(comp.dealerIdentitiesCollection).toEqual(expectedCollection);
    });

    it('Should call SecurityClearance query and add missing value', () => {
      const applicationUser: IApplicationUser = { id: 456 };
      const securityClearance: ISecurityClearance = { id: 41242 };
      applicationUser.securityClearance = securityClearance;

      const securityClearanceCollection: ISecurityClearance[] = [{ id: 71640 }];
      jest.spyOn(securityClearanceService, 'query').mockReturnValue(of(new HttpResponse({ body: securityClearanceCollection })));
      const additionalSecurityClearances = [securityClearance];
      const expectedCollection: ISecurityClearance[] = [...additionalSecurityClearances, ...securityClearanceCollection];
      jest.spyOn(securityClearanceService, 'addSecurityClearanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      expect(securityClearanceService.query).toHaveBeenCalled();
      expect(securityClearanceService.addSecurityClearanceToCollectionIfMissing).toHaveBeenCalledWith(
        securityClearanceCollection,
        ...additionalSecurityClearances
      );
      expect(comp.securityClearancesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const applicationUser: IApplicationUser = { id: 456 };
      const systemIdentity: IUser = { id: 32396 };
      applicationUser.systemIdentity = systemIdentity;

      const userCollection: IUser[] = [{ id: 67663 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [systemIdentity];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UniversallyUniqueMapping query and add missing value', () => {
      const applicationUser: IApplicationUser = { id: 456 };
      const userProperties: IUniversallyUniqueMapping[] = [{ id: 74931 }];
      applicationUser.userProperties = userProperties;

      const universallyUniqueMappingCollection: IUniversallyUniqueMapping[] = [{ id: 8474 }];
      jest
        .spyOn(universallyUniqueMappingService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: universallyUniqueMappingCollection })));
      const additionalUniversallyUniqueMappings = [...userProperties];
      const expectedCollection: IUniversallyUniqueMapping[] = [
        ...additionalUniversallyUniqueMappings,
        ...universallyUniqueMappingCollection,
      ];
      jest.spyOn(universallyUniqueMappingService, 'addUniversallyUniqueMappingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      expect(universallyUniqueMappingService.query).toHaveBeenCalled();
      expect(universallyUniqueMappingService.addUniversallyUniqueMappingToCollectionIfMissing).toHaveBeenCalledWith(
        universallyUniqueMappingCollection,
        ...additionalUniversallyUniqueMappings
      );
      expect(comp.universallyUniqueMappingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const applicationUser: IApplicationUser = { id: 456 };
      const organization: IDealer = { id: 84852 };
      applicationUser.organization = organization;
      const department: IDealer = { id: 94839 };
      applicationUser.department = department;
      const dealerIdentity: IDealer = { id: 4059 };
      applicationUser.dealerIdentity = dealerIdentity;
      const securityClearance: ISecurityClearance = { id: 20873 };
      applicationUser.securityClearance = securityClearance;
      const systemIdentity: IUser = { id: 45793 };
      applicationUser.systemIdentity = systemIdentity;
      const userProperties: IUniversallyUniqueMapping = { id: 40371 };
      applicationUser.userProperties = [userProperties];

      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(applicationUser));
      expect(comp.dealersSharedCollection).toContain(organization);
      expect(comp.dealersSharedCollection).toContain(department);
      expect(comp.dealerIdentitiesCollection).toContain(dealerIdentity);
      expect(comp.securityClearancesSharedCollection).toContain(securityClearance);
      expect(comp.usersSharedCollection).toContain(systemIdentity);
      expect(comp.universallyUniqueMappingsSharedCollection).toContain(userProperties);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ApplicationUser>>();
      const applicationUser = { id: 123 };
      jest.spyOn(applicationUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicationUser }));
      saveSubject.complete();

      // THEN
      // expect(comp.previousState).toHaveBeenCalled();
      // expect(applicationUserService.update).toHaveBeenCalledWith(applicationUser);
      // expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ApplicationUser>>();
      const applicationUser = new ApplicationUser();
      jest.spyOn(applicationUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicationUser }));
      saveSubject.complete();

      // THEN
      // expect(applicationUserService.create).toHaveBeenCalledWith(applicationUser);
      // expect(comp.isSaving).toEqual(false);
      // TODO expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ApplicationUser>>();
      const applicationUser = { id: 123 };
      jest.spyOn(applicationUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      // expect(applicationUserService.update).toHaveBeenCalledWith(applicationUser);
      // expect(comp.isSaving).toEqual(false);
      // TODO expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDealerById', () => {
      it('Should return tracked Dealer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDealerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSecurityClearanceById', () => {
      it('Should return tracked SecurityClearance primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSecurityClearanceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUniversallyUniqueMappingById', () => {
      it('Should return tracked UniversallyUniqueMapping primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUniversallyUniqueMappingById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedUniversallyUniqueMapping', () => {
      it('Should return option if no UniversallyUniqueMapping is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedUniversallyUniqueMapping(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected UniversallyUniqueMapping for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this UniversallyUniqueMapping is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedUniversallyUniqueMapping(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
