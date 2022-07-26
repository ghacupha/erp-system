Design Notes
============

Amortization of prepayments
****************************

Many of the system's modules are designed to update information that eventually will be associated with
assets. Prepayments is one of those as you might realize some assets can be recognised by a posting
that credits a prepayment account and debiting the corresponding asset account.

```
   DR. Corresponding Asset A/c
   CR. Prepayment A/C

This necessitates setup of full blown prepayments accounting and as is commonly intended prepayment
accounts are usually prepaid expenses rather than prepaid assets, so ever so often the organization
needs to effect amortization transactions such as the following:

```
   DR. Expense A/C
   CR. Prepayment A/C

The basic philosophy of amortization
------------------------------------

Once you find yourself dealing with multiples of accounts, with varying amortization periods in various
accounts and service outlets, you will realize you have a mess in your hands, just the sort of thing that
we happily prefer to be automated.

Such a platform would need to provide periodic reports ascertaining the positions at any given time in the
life of the accounts; you do have to justify some global figure of total prepayments, so that an auditor
can satisfy themselves that the account has not been used to hide assets (most times when you are amortizing
for a period of more than a year, you are dealing with an asset), or reduce the expense figure reported
for a given period.

NB: Some thrift members of our beloved profession have been known to postpone expenses
by simply posting them as prepayments, therefore moving those to the assets on the balance sheet rather
than the dreaded expense lines on the profit and loss, in order to amortize the same in some preferred or
favorable period in the way that might coincide with some act of market signaling or to align oneself to some
more immediate dividend policy in a given period. This may alter the financial statements ability to provide
insights into the economic well-being and reality of an organization, but who am I to judge?

Having such an entity can help us maintain even more data concerning the history of an asset by correlating
its origins to payments that were initially considered as prepayments.

Amortization needs to be implemented in a way that is as flexible as possible and that reduces magic (at least from
the end user's perspective) as much as possible. This means that we would prefer not to carry out transactions automatically
via the system, in any scenario where the same can be done manually by user invocation while retaining all that
juicy audit information about, who did what when why, and how did they justify their actions.

This design may sound arbitrarily tedious to an end user, but the intention is to eventually link the process to a
business process engine enabling collaboration amongst users with varying or increasing levels of oversight with
each incremental step. This would also allow a transaction to have the desired trait of "reversibility" and position
reports at every step should an error be discovered. This will also make the code easier to maintain (believe it or
not as separate events are registered in multiple of entities and not single block of interconnected batch processes.


