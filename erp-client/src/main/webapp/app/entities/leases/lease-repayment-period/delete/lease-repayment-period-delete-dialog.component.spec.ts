jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { LeaseRepaymentPeriodService } from '../service/lease-repayment-period.service';

import { LeaseRepaymentPeriodDeleteDialogComponent } from './lease-repayment-period-delete-dialog.component';

describe('LeaseRepaymentPeriod Management Delete Component', () => {
  let comp: LeaseRepaymentPeriodDeleteDialogComponent;
  let fixture: ComponentFixture<LeaseRepaymentPeriodDeleteDialogComponent>;
  let service: LeaseRepaymentPeriodService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LeaseRepaymentPeriodDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(LeaseRepaymentPeriodDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseRepaymentPeriodDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LeaseRepaymentPeriodService);
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
