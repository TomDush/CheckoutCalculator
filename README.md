Checkout Calculator
===================

Compile and install
-------------------

This is a simple _Maven_ project. Just run in root directory (where this file is):

    mvn clean install

You can start your read by test case: _fr.dush.checkout.CheckoutCalculatorTest_.

Assumptions
-----------

The goal of this project is to show Java development skills and problem solving. Because of that, I don't use on
purpose any external library like a rules engine ( _Drools_ for example).
For this same reason and time saving there is no user interface (even text) neither real data persistence. Please let
me know if you need more.

Because it's (on purpose) a home-made rule engine, here the limitation:
 * Can't add rules at runtime. This is actually possible if it reuse a type (an implementation) already existing
 * Persistence layer hasn't been added: rules are loaded programmatically. However, This can be added easily...
 * There are no rule order, or exclusion, management. Rules implementation and configuration must be done knowing other
 rules can have already added discounts or gifts.

That's why rules are hard coded in Java. Only parameters can be changed.

Order workflow is simple. It do not required workflow engine, or route engine, and it can be done in a simple
service.

Currency is implicitly known and always the same

Separation of concerns
----------------------

Here how I separate concerns:

 * Order persistence -> even if I only did simple in-memory implementations, domain objects are created to be
 persistent entities (JPA or NoSQL database, doesn't matter) ; or/and become DTO.
 They are enriched throw order workflow (with discounts and gifts) for simplicity (better than different objects
 with complex relations)

 * Rules -> they all are _Rule_ interface implementations. They add what they want to order (wither discounts, or gifts)

 * Invoicing -> implemented in InvoicingService, it compute grand total (and sub-total). It's presented as main business
 service to finish an order, thus it save it and run rules on it. This should be again separated if workflow become
 harder.

 * Receipt printer -> Implemented by ReceiptWriterImpl, it should be only a view of the finished order. Only calculation
 in it is to display what item is free and actual price of discounted items.


I choose BigDecimal to represent prices, but this is certainly an error. A simple long representing price in cents
would have done a better and a simple choice in this case to prevent any round errors.