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

import { PrepaymentsNavComponent } from './prepayments-nav.component';

jest.mock('@angular/router');
jest.mock('app/login/login.service');

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { NgxWebstorageModule } from 'ngx-webstorage';

import { ProfileInfo } from 'app/layouts/profiles/profile-info.model';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { LoginService } from 'app/login/login.service';

describe('Component Tests', () => {
  describe('Prepayments Nav Component', () => {
    let comp: PrepaymentsNavComponent;
    let fixture: ComponentFixture<PrepaymentsNavComponent>;
    let accountService: AccountService;
    let profileService: ProfileService;
    const account: Account = {
      activated: true,
      authorities: [],
      email: '',
      firstName: 'John',
      langKey: '',
      lastName: 'Doe',
      login: 'john.doe',
      imageUrl: '',
    };

    beforeEach(
      waitForAsync(() => {
        TestBed.configureTestingModule({
          imports: [HttpClientTestingModule, NgxWebstorageModule.forRoot()],
          declarations: [PrepaymentsNavComponent],
          providers: [Router, LoginService],
        })
          .overrideTemplate(PrepaymentsNavComponent, '')
          .compileComponents();
      })
    );

    beforeEach(() => {
      fixture = TestBed.createComponent(PrepaymentsNavComponent);
      comp = fixture.componentInstance;
      accountService = TestBed.inject(AccountService);
      profileService = TestBed.inject(ProfileService);
    });

    it('Should call profileService.getProfileInfo on init', () => {
      // GIVEN
      jest.spyOn(profileService, 'getProfileInfo').mockReturnValue(of(new ProfileInfo()));

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(profileService.getProfileInfo).toHaveBeenCalled();
    });

    it('Should hold current authenticated user in variable account', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.account).toBeNull();

      // WHEN
      accountService.authenticate(account);

      // THEN
      expect(comp.account).toEqual(account);

      // WHEN
      accountService.authenticate(null);

      // THEN
      expect(comp.account).toBeNull();
    });

    it('Should hold current authenticated user in variable account if user is authenticated before page load', () => {
      // GIVEN
      accountService.authenticate(account);

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.account).toEqual(account);

      // WHEN
      accountService.authenticate(null);

      // THEN
      expect(comp.account).toBeNull();
    });
  });
});
