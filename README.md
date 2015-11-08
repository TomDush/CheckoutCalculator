Checkout Calculator
===================

Compile and install
-------------------

This is a simple _Maven_ project. Just run in root directory (where this file is):

    mvn clean install

To use it, run after compilation:

    java -jar target/checkout-0.1-SNAPSHOT

You'll have an invite command to choose what items you want to take.


Assumptions
-----------

The goal of this project is to implement a kind of rule engine.

That's why parts "user interface" and "data persistence" are made a simple as possible: text interface and in-memory persistence.
It also why I'm not using a proper rule engine like **Drools**.

But even with this assumptions, I made my best to keep improvement possible by using libraries like *Spring Data JPA* or *Drools*, and any valid interface.


Concern Spitting
----------------

Here the logic I followed to design this app:
 * 