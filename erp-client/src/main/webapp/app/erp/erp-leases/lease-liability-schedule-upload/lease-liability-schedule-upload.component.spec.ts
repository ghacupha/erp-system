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
