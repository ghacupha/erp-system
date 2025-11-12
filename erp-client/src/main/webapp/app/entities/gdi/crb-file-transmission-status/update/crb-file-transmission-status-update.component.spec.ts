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

import { CrbFileTransmissionStatusService } from '../service/crb-file-transmission-status.service';
import { ICrbFileTransmissionStatus, CrbFileTransmissionStatus } from '../crb-file-transmission-status.model';

import { CrbFileTransmissionStatusUpdateComponent } from './crb-file-transmission-status-update.component';

describe('CrbFileTransmissionStatus Management Update Component', () => {
  let comp: CrbFileTransmissionStatusUpdateComponent;
  let fixture: ComponentFixture<CrbFileTransmissionStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbFileTransmissionStatusService: CrbFileTransmissionStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbFileTransmissionStatusUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbFileTransmissionStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbFileTransmissionStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbFileTransmissionStatusService = TestBed.inject(CrbFileTransmissionStatusService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbFileTransmissionStatus: ICrbFileTransmissionStatus = { id: 456 };

      activatedRoute.data = of({ crbFileTransmissionStatus });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbFileTransmissionStatus));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbFileTransmissionStatus>>();
      const crbFileTransmissionStatus = { id: 123 };
      jest.spyOn(crbFileTransmissionStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbFileTransmissionStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbFileTransmissionStatus }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbFileTransmissionStatusService.update).toHaveBeenCalledWith(crbFileTransmissionStatus);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbFileTransmissionStatus>>();
      const crbFileTransmissionStatus = new CrbFileTransmissionStatus();
      jest.spyOn(crbFileTransmissionStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbFileTransmissionStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbFileTransmissionStatus }));
      saveSubject.complete();

      // THEN
      expect(crbFileTransmissionStatusService.create).toHaveBeenCalledWith(crbFileTransmissionStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbFileTransmissionStatus>>();
      const crbFileTransmissionStatus = { id: 123 };
      jest.spyOn(crbFileTransmissionStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbFileTransmissionStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbFileTransmissionStatusService.update).toHaveBeenCalledWith(crbFileTransmissionStatus);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
