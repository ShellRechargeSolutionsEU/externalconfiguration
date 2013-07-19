# External Configuration [![Build Status](https://secure.travis-ci.org/thenewmotion/externalconfiguration.png)](http://travis-ci.org/thenewmotion/externalconfiguration)

## Examples


### Manual use

```scala
  // by default it will configure logback, typesafe config, lift props
  ExternalConfiguration.load()


  // if default options are not enough for you
  val myConfConfigurable: ExternalConfiguration.Configurable = propsDirPath => {
    val myConf = propsDirPath + "/myconf.myconf"
    // use myconf
  }

  ExternalConfiguration.load(ExternalConfiguration.defaultConfigurables + myConfConfigurable)
```

### Spray web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">
    <listener>
        <listener-class>com.thenewmotion.externalconfiguration.Listener</listener-class>
    </listener>

    <filter>
        <filter-name>LiftFilter</filter-name>
        <filter-class>net.liftweb.http.LiftFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>LiftFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
</web-app>
```


### Lift web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">

    <listener>
        <listener-class>com.thenewmotion.externalconfiguration.Listener</listener-class>
    </listener>

    <listener>
        <listener-class>spray.servlet.Initializer</listener-class>
    </listener>

    <servlet>
        <servlet-name>SprayConnectorServlet</servlet-name>
        <servlet-class>spray.servlet.Servlet30ConnectorServlet</servlet-class>
        <async-supported>true</async-supported>
    </servlet>

    <servlet-mapping>
        <servlet-name>SprayConnectorServlet</servlet-name>
        <url-pattern>*</url-pattern>
    </servlet-mapping>
</web-app>
```



## Setup

1. Add this repository to your pom.xml:
```xml
    <repository>
        <id>thenewmotion</id>
        <name>The New Motion Repository</name>
        <url>http://nexus.thenewmotion.com/content/groups/public/</url>
    </repository>
```

2. Add dependency to your pom.xml:
```xml
    <dependency>
        <groupId>com.thenewmotion</groupId>
        <artifactId>externalconfiguration_2.10</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
```
