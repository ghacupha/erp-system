import {State} from "./global-store.definition";
import {dealerPaymentSelectedDealer, dealerPaymentStatus} from "./dealer-workflows-status.selectors";

describe('Selectors', () => {
  describe('Dealer workflows status selectors', () => {

    let initialState: State;
    beforeEach(() => {
      initialState = {
        paymentsFormState: {
          selectedPayment: {},
          weAreCopying: false,
          weAreEditing: false,
          weAreCreating: false,
        },
        dealerWorkflowState: {
          selectedDealer: {
            id: 1150,
            dealerName: 'ERP Systems ltd'
          },
          weArePayingADealer: true
        }
      }
    })

    it('should select the selected-dealer', function () {

      const testResults = dealerPaymentSelectedDealer.projector(
        initialState.dealerWorkflowState
      );

      expect(testResults.id).toEqual(1150);
      expect(testResults.dealerName).toEqual('ERP Systems ltd')
    });

    it('should select the dealer payment status', function () {
      const testResults = dealerPaymentStatus.projector(
        initialState.dealerWorkflowState
      );

      expect(testResults).toEqual(true);
    });

  });
});
