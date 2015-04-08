## Fintrack - Personal Financial Tracking Software

Powered by Java EE


### License

The MIT License (MIT)

Copyright © 2012 Arvid Juskaitis <arvydas.juskaitis@gmail.com>



### Overview

This project is very similar to [fintrack-php](https://github.com/arvjus/fintrack-php) in terms of functionality, UI, data model.

Some highlights of the project:

* Front-end is implemented by JSP, jQuery
* Uses own MVC, Command pattern implementation
* Uses own Templating tag library
* Back-end is implemented by stateless EJB, JPA, PostgreSQL
* Embedded OpenEJB, DBUnit used for unitests
* It has REST API + client application for Android



### Prerequisites

* Java 5 or later
* PostgreSQL 8.x
* Maven 2.x



### Installation


Install PostgreSQL; goto fintrack-model/src/main/db and run "ant ci test" to create, populate databases

Copy openejb.war to <tomcat>/webapps; load http://localhost:8080/openejb and run install

Copy postgresql jdbc driver to <tomcat>/lib

Create a user with "viewer", "reporter", "admin" roles in tomcat.

Add fintrack_test datasource to <tomcat>/conf/openejb.xml

Build common-mvc, common-taglib dependencies, and finally fintrack; copy fintrack.war to <tomcat>/webapps


Go to [http://localhost:8080/fintrack](http://localhost:8080/fintrack)

