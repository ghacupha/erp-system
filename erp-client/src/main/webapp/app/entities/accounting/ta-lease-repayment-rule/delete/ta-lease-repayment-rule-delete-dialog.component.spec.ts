jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TALeaseRepaymentRuleService } from '../service/ta-lease-repayment-rule.service';

import { TALeaseRepaymentRuleDeleteDialogComponent } from './ta-lease-repayment-rule-delete-dialog.component';

describe('TALeaseRepaymentRule Management Delete Component', () => {
  let comp: TALeaseRepaymentRuleDeleteDialogComponent;
  let fixture: ComponentFixture<TALeaseRepaymentRuleDeleteDialogComponent>;
  let service: TALeaseRepaymentRuleService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TALeaseRepaymentRuleDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(TALeaseRepaymentRuleDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TALeaseRepaymentRuleDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TALeaseRepaymentRuleService);
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
