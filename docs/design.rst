Design Notes
============

Amortization of prepayments
****************************

Many of the system's modules are designed to update information that eventually will be associated with
assets. Prepayments is one of those as you might realize some assets can be recognised by a posting
that credits a prepayment account and debiting the corresponding asset account.

.. note::
   DR. Corresponding Asset A/c
   CR. Prepayment A/C

This necessitates setup of full blown prepayments accounting and as is commonly intended prepayment
accounts are usually prepaid expenses rather than prepaid assets, so ever so often the organization
needs to effect amortization transactions such as the following:

.. note::
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

.. note::
    Some thrift members of our beloved profession have been known to postpone expenses
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
not) as separate events are registered in multiple of entities and not single block of interconnected batch processes.

Implementation
------------------

The above philosophy may not be complete and is still under review. Therefore the following work is still in progress
and even the code itself remains mostly unimplemented.

The main entities are as follows:
 - prepayment-account
 - amortization-report
 - prepayment-balance
 - amortization-recurrence
 - amortization-sequence
 - amortization-period

 The **prepayment-account** is a register that relates the existence of the transaction, the accounts posted and accounts
 to post when amortizing.

 The **amortization-report** tells about what was amortised and does not itself define amortization. As such it does not have to
 be defined at the setup of the prepayment-account for the model to be complete. It exists just so that a reporting system
 can pick out positions at different points in time. Multiples of instances could exist for a given prepayment at a given time
 period, only that one will be flagged to be active. This will allow for inexpensive corrections, in that an instance is either
 acknowledged as expense for a period or ignored.

 The **prepayment-balance** is also a reporting entity, that exists to provide a calculated value of outstanding prepayment
 balance as at a given point in time.

 The **amortization-recurrence** entity exists to define, how amortization is to occur. It configures the following about the prepayment
 - active: boolean
 - first_amortization_date: local-date
 - frequency: enum(Maintenance Frequency => Monthly, Bimonthly, Quarterly, Trimesters, Biannual)
 - recurrences: integer
 - notes: text
 - particulars: string
 - overwritten: boolean
 - installed_on: local-date-time

 The last flag indicates whether the amortization-recurrence instance has been superseded by a descendant. This means
 that you could have multiples of recurrence instances as long as only one is not overwritten. This also allows quick
 implementation of off-now-on-again kind of sequence should such be desired when one decides to second-guess their actions.
 The important thing is once we wish to amend, we don't modify the instance, instead we create another and flag the ancestor
 as overwritten, and the control goes to the successor. You can then have multiples of ancestors as long as they are
 overwritten and inactive.

 At least one recurrence instance should ideally exist for each prepayment-account and a system report should flag
 otherwise cases as to-do items for the user.

 The installed_on variable enables the system to track precedence of recurrence objects should an automated cleanup or
 pruning process be desired or even one that is invoked by a well-advised user.

 The  **amortization-sequence** entity tracks the events
 - latest_amortization_date: local-date
 - next_amortization_date: local-date
 - previous_amortization_date: local-date

 This means we can independently track the amortization date of a prepayment and know when it is due for prepayment. This is
 logic proceeds from the recurrence when dictates the frequency with which amortization occurs whether monthly, quarterly,
 half-yearly and so on and so forth.

 **Day one** is a configuration which dictates a cut off date considered the initial date for recognition of prepayments,
 amortization-report and prepayment-balance. No sequence can therefore have a last_amortization_date that is earlier than the
 day-one value.

 An amortization-sequence will have a many to one relationship with recurrence and the prepayment-account. The recurrence
 idealogically has a one to one relationship with the prepayment account. Idealogically because if we want to make recurrence
 instances immutable we will need to define many to one relationships instead.

 The end-user's amortization request object will likely (man, am still designing this thing) contain a period designated by a
 start-date and an end-date. During the staging session, the process will ignore the entities whose last amortization date is later than the request object
 start date AND the next amortization date is also later than the request object's end date. Everything else will be
 considered for further amortization. So before the actual amortization run the use will have a way of knowing which
 entities are due for amortization within the request object's parameters.

 Reporting Period (work in progress)
 --------------------------------------
 It's not yet clicked how but we'll have to create another entity called reporting-period for which we can obtain
 a report from both the amortization-report and prepayment-balance entities which will have some kind of relationship with the same


