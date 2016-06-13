# Rapid prototyping with Spring Boot and AngularJS
This example demonstrates how **Spring Boot**, **Spring Data JPA** and in the front-end **AngularJS** can be used together to write web applications easily.
In this code example I'm demonstrating this by providing a full CRUD-based web application in about 150 lines of code (100 lines of Java code and 50 lines of JavaScript code).

## Frameworks

### Front-end

#### Twitter Bootstrap
For rapidly creating prototypes of a web application, a UI toolkit or library will become really handy. There are many choices available, and for this example I chose Twitter Bootstrap.

#### AngularJS
AngularJS is a MVC based framework for web applications, written in JavaScript. It makes it possible to use the Model-View-Controller pattern on the front-end. It also comes with several additional modules. In this example I'm also using **angular-resource**, which is a simple factory-pattern based module for creating REST clients.

### Back-end

#### Spring Boot
One of the hassles while creating web applications using the Spring Framework is that it involves a lot of configuration. Spring Boot makes it possible to write configuration-less web application because it does a lot for you out of the box.
For example, if you add HSQLDB as a dependency to your application, it will automatically provide a datasource to it.
If you add the spring-boot-starter-web dependency, then you can start writing controllers for creating a web application.

#### Spring Data JPA
Spring Data JPA allows you to create repositories for your data without even having to write a lot of code. The only code you need is a simple interface that extends from another interface and then you're done.
With Spring Boot you can even leave the configuration behind for configuring Spring Data JPA, so now it's even easier.

## Installation
Installation is quite easy, first you will have to install some front-end dependencies using Bower:
```
bower install
```

Then you can run Maven to package the application:
```
mvn clean package
```

Now you can run the Java application quite easily:
```
cd target
java -jar ng-spring-boot-1.0.0.jar
```

## Related articles

- [Rapid prototyping with Spring Boot and AngularJS](http://g00glen00b.be/prototyping-spring-boot-angularjs/)
- [Easy integration testing with Spring Boot and REST-Assured](http://g00glen00b.be/spring-boot-rest-assured/)
- [Unit testing with Mockito and AssertJ](http://g00glen00b.be/unit-testing-mockito-assertj/)
- [Executing Jasmine tests with Maven](http://g00glen00b.be/jasmine-tests-maven/)
- [Testing your Spring Data JPA repository](http://g00glen00b.be/testing-spring-data-repository/)
- [Testing your Spring Boot application with Selenium](http://g00glen00b.be/spring-boot-selenium/)

