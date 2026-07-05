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

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { LeaseLiabilityScheduleUploadComponent } from './lease-liability-schedule-upload.component';
import { LeaseLiabilityScheduleUploadService } from './service/lease-liability-schedule-upload.service';

describe('LeaseLiabilityScheduleUploadComponent', () => {
  let component: LeaseLiabilityScheduleUploadComponent;
  let fixture: ComponentFixture<LeaseLiabilityScheduleUploadComponent>;
  let service: jasmine.SpyObj<LeaseLiabilityScheduleUploadService>;

  beforeEach(async () => {
    service = jasmine.createSpyObj('LeaseLiabilityScheduleUploadService', ['upload']);

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [LeaseLiabilityScheduleUploadComponent],
      providers: [FormBuilder, { provide: LeaseLiabilityScheduleUploadService, useValue: service }],
    })
      .compileComponents();

    fixture = TestBed.createComponent(LeaseLiabilityScheduleUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should call upload service when form is valid and file selected', () => {
    const leaseLiability = { id: 10 } as any;
    component.editForm.patchValue({ leaseLiability });
    component.selectedFile = new File(['id,amount'], 'schedule.csv', { type: 'text/csv' });

    service.upload.and.returnValue(of(new HttpResponse({ body: { uploadId: 1 } })));

    component.submitUpload();

    expect(service.upload).toHaveBeenCalled();
    const args = service.upload.calls.mostRecent().args[0];
    expect(args.leaseLiabilityId).toBe(leaseLiability.id);
  });
});
