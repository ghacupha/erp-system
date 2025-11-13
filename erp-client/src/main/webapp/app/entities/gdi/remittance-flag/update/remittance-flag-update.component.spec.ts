///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import { RemittanceFlagService } from '../service/remittance-flag.service';
import { IRemittanceFlag, RemittanceFlag } from '../remittance-flag.model';

import { RemittanceFlagUpdateComponent } from './remittance-flag-update.component';

describe('RemittanceFlag Management Update Component', () => {
  let comp: RemittanceFlagUpdateComponent;
  let fixture: ComponentFixture<RemittanceFlagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let remittanceFlagService: RemittanceFlagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RemittanceFlagUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RemittanceFlagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RemittanceFlagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    remittanceFlagService = TestBed.inject(RemittanceFlagService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const remittanceFlag: IRemittanceFlag = { id: 456 };

      activatedRoute.data = of({ remittanceFlag });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(remittanceFlag));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RemittanceFlag>>();
      const remittanceFlag = { id: 123 };
      jest.spyOn(remittanceFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ remittanceFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: remittanceFlag }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(remittanceFlagService.update).toHaveBeenCalledWith(remittanceFlag);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RemittanceFlag>>();
      const remittanceFlag = new RemittanceFlag();
      jest.spyOn(remittanceFlagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ remittanceFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: remittanceFlag }));
      saveSubject.complete();

      // THEN
      expect(remittanceFlagService.create).toHaveBeenCalledWith(remittanceFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RemittanceFlag>>();
      const remittanceFlag = { id: 123 };
      jest.spyOn(remittanceFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ remittanceFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(remittanceFlagService.update).toHaveBeenCalledWith(remittanceFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
