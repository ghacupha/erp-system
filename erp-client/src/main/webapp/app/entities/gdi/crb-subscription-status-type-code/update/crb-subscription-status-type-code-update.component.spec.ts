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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CrbSubscriptionStatusTypeCodeService } from '../service/crb-subscription-status-type-code.service';
import { ICrbSubscriptionStatusTypeCode, CrbSubscriptionStatusTypeCode } from '../crb-subscription-status-type-code.model';

import { CrbSubscriptionStatusTypeCodeUpdateComponent } from './crb-subscription-status-type-code-update.component';

describe('CrbSubscriptionStatusTypeCode Management Update Component', () => {
  let comp: CrbSubscriptionStatusTypeCodeUpdateComponent;
  let fixture: ComponentFixture<CrbSubscriptionStatusTypeCodeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbSubscriptionStatusTypeCodeService: CrbSubscriptionStatusTypeCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbSubscriptionStatusTypeCodeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbSubscriptionStatusTypeCodeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbSubscriptionStatusTypeCodeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbSubscriptionStatusTypeCodeService = TestBed.inject(CrbSubscriptionStatusTypeCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbSubscriptionStatusTypeCode: ICrbSubscriptionStatusTypeCode = { id: 456 };

      activatedRoute.data = of({ crbSubscriptionStatusTypeCode });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbSubscriptionStatusTypeCode));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbSubscriptionStatusTypeCode>>();
      const crbSubscriptionStatusTypeCode = { id: 123 };
      jest.spyOn(crbSubscriptionStatusTypeCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbSubscriptionStatusTypeCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbSubscriptionStatusTypeCode }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbSubscriptionStatusTypeCodeService.update).toHaveBeenCalledWith(crbSubscriptionStatusTypeCode);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbSubscriptionStatusTypeCode>>();
      const crbSubscriptionStatusTypeCode = new CrbSubscriptionStatusTypeCode();
      jest.spyOn(crbSubscriptionStatusTypeCodeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbSubscriptionStatusTypeCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbSubscriptionStatusTypeCode }));
      saveSubject.complete();

      // THEN
      expect(crbSubscriptionStatusTypeCodeService.create).toHaveBeenCalledWith(crbSubscriptionStatusTypeCode);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbSubscriptionStatusTypeCode>>();
      const crbSubscriptionStatusTypeCode = { id: 123 };
      jest.spyOn(crbSubscriptionStatusTypeCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbSubscriptionStatusTypeCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbSubscriptionStatusTypeCodeService.update).toHaveBeenCalledWith(crbSubscriptionStatusTypeCode);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
