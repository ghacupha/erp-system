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

import { StaffCurrentEmploymentStatusService } from '../service/staff-current-employment-status.service';
import { IStaffCurrentEmploymentStatus, StaffCurrentEmploymentStatus } from '../staff-current-employment-status.model';

import { StaffCurrentEmploymentStatusUpdateComponent } from './staff-current-employment-status-update.component';

describe('StaffCurrentEmploymentStatus Management Update Component', () => {
  let comp: StaffCurrentEmploymentStatusUpdateComponent;
  let fixture: ComponentFixture<StaffCurrentEmploymentStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let staffCurrentEmploymentStatusService: StaffCurrentEmploymentStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [StaffCurrentEmploymentStatusUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(StaffCurrentEmploymentStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StaffCurrentEmploymentStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    staffCurrentEmploymentStatusService = TestBed.inject(StaffCurrentEmploymentStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const staffCurrentEmploymentStatus: IStaffCurrentEmploymentStatus = { id: 456 };

      activatedRoute.data = of({ staffCurrentEmploymentStatus });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(staffCurrentEmploymentStatus));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StaffCurrentEmploymentStatus>>();
      const staffCurrentEmploymentStatus = { id: 123 };
      jest.spyOn(staffCurrentEmploymentStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ staffCurrentEmploymentStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: staffCurrentEmploymentStatus }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(staffCurrentEmploymentStatusService.update).toHaveBeenCalledWith(staffCurrentEmploymentStatus);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StaffCurrentEmploymentStatus>>();
      const staffCurrentEmploymentStatus = new StaffCurrentEmploymentStatus();
      jest.spyOn(staffCurrentEmploymentStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ staffCurrentEmploymentStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: staffCurrentEmploymentStatus }));
      saveSubject.complete();

      // THEN
      expect(staffCurrentEmploymentStatusService.create).toHaveBeenCalledWith(staffCurrentEmploymentStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StaffCurrentEmploymentStatus>>();
      const staffCurrentEmploymentStatus = { id: 123 };
      jest.spyOn(staffCurrentEmploymentStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ staffCurrentEmploymentStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(staffCurrentEmploymentStatusService.update).toHaveBeenCalledWith(staffCurrentEmploymentStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
