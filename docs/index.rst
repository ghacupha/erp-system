.. include:: ../README.rst

Welcome to ERP-SYSTEM documentation!
===================================

**ERP-SYSTEM** is a Java application for accountants and accounts admins
that records the kind of financial information that you don't normally have on a
typical accounts management app.

It began as an effort to record and maintain data for fixed assets management and
various iterations have been made and failed for one reason or another. The very first
was a commandline application, and the reasons for it's failure could not be more
obvious. It did serve as an important lesson though, that such a system would need
collaboration among users and admins of the data and hence, the architecture needs to be
web based and if the client is not web based, the data would nevertheless need to
be accessed from different avenues and so the design cannot work if data is
orchestrated in some local database.

Using jhipster we've spawn dozens of entities in many modules and all of those, all that
effort was  to have more information on the fixed assets of an organization. Mid-development
IFRS16 became a thing in the world of accounting and so that too became part of the
project goals, because we wanted to manage those "right of use" assets and so you will find that
the project has evolved to record various aspects of an organization's transactions even when
fixed-assets management is no longer the end goal.

Check out the :doc:`usage` section for further information, including
how to :ref:`install` the project.

.. note::

   This project is under active development.

Contents
--------

.. toctree::

   Home <self>
   usage
   api
   milestones
   design
