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

import { AcquiringIssuingFlagService } from '../service/acquiring-issuing-flag.service';
import { IAcquiringIssuingFlag, AcquiringIssuingFlag } from '../acquiring-issuing-flag.model';

import { AcquiringIssuingFlagUpdateComponent } from './acquiring-issuing-flag-update.component';

describe('AcquiringIssuingFlag Management Update Component', () => {
  let comp: AcquiringIssuingFlagUpdateComponent;
  let fixture: ComponentFixture<AcquiringIssuingFlagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let acquiringIssuingFlagService: AcquiringIssuingFlagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AcquiringIssuingFlagUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AcquiringIssuingFlagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AcquiringIssuingFlagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    acquiringIssuingFlagService = TestBed.inject(AcquiringIssuingFlagService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const acquiringIssuingFlag: IAcquiringIssuingFlag = { id: 456 };

      activatedRoute.data = of({ acquiringIssuingFlag });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(acquiringIssuingFlag));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AcquiringIssuingFlag>>();
      const acquiringIssuingFlag = { id: 123 };
      jest.spyOn(acquiringIssuingFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acquiringIssuingFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: acquiringIssuingFlag }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(acquiringIssuingFlagService.update).toHaveBeenCalledWith(acquiringIssuingFlag);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AcquiringIssuingFlag>>();
      const acquiringIssuingFlag = new AcquiringIssuingFlag();
      jest.spyOn(acquiringIssuingFlagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acquiringIssuingFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: acquiringIssuingFlag }));
      saveSubject.complete();

      // THEN
      expect(acquiringIssuingFlagService.create).toHaveBeenCalledWith(acquiringIssuingFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AcquiringIssuingFlag>>();
      const acquiringIssuingFlag = { id: 123 };
      jest.spyOn(acquiringIssuingFlagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acquiringIssuingFlag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(acquiringIssuingFlagService.update).toHaveBeenCalledWith(acquiringIssuingFlag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
