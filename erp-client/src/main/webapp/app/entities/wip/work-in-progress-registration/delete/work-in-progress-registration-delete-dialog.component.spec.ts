jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { WorkInProgressRegistrationService } from '../service/work-in-progress-registration.service';

import { WorkInProgressRegistrationDeleteDialogComponent } from './work-in-progress-registration-delete-dialog.component';

describe('WorkInProgressRegistration Management Delete Component', () => {
  let comp: WorkInProgressRegistrationDeleteDialogComponent;
  let fixture: ComponentFixture<WorkInProgressRegistrationDeleteDialogComponent>;
  let service: WorkInProgressRegistrationService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [WorkInProgressRegistrationDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(WorkInProgressRegistrationDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WorkInProgressRegistrationDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(WorkInProgressRegistrationService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
