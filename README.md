# Mis notas

kafka

```bash
  janrax@janrax-Legion-5-Linux:~/Escritorio/Code/epcsd-spring-2023-productcatalog$ docker-compose ps
  Name                                  Command               State                                            Ports
  --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  epcsd-spring-2023-productcatalog_adminer_1     entrypoint.sh php -S [::]: ...   Up      0.0.0.0:18080->8080/tcp,:::18080->8080/tcp                                              
  epcsd-spring-2023-productcatalog_kafka_1       /etc/confluent/docker/run        Up      0.0.0.0:19092->19092/tcp,:::19092->19092/tcp, 0.0.0.0:29092->9092/tcp,:::29092->9092/tcp
  epcsd-spring-2023-productcatalog_productdb_1   docker-entrypoint.sh postgres    Up      0.0.0.0:54320->5432/tcp,:::54320->5432/tcp                                              
  epcsd-spring-2023-productcatalog_userdb_1      docker-entrypoint.sh postgres    Up      0.0.0.0:54321->5432/tcp,:::54321->5432/tcp                                              
  epcsd-spring-2023-productcatalog_zookeeper_1   /etc/confluent/docker/run        Up      0.0.0.0:22181->2181/tcp,:::22181->2181/tcp, 2888/tcp, 3888/tcp



  janrax@janrax-Legion-5-Linux:~/Escritorio/Code/epcsd-spring-2023-productcatalog$ docker exec -it epcsd-spring-2023-productcatalog_kafka_1 bash

  [appuser@a3a6858689f7 /bin]$ kaftopics --create --topic mytopic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
  
  [appuser@a3a6858689f7 /bin]$ kafka-console-producer --topic mitopic --bootstrap-server localhost:9092
  >hola
  >que pasa
  
  # Desde otra sesión
  [appuser@a3a6858689f7 bin]$ kafka-console-consumer --topic mitopic --bootstrap-server localhost:9092 --from-beginning
  hola
  que pasa

```

<div id="top"></div>
<!--
*** Made using the Best-README-Template
*** https://github.com/othneildrew/Best-README-Template/blob/master/README.md
-->


<!-- PROJECT LOGO -->
<br />
<div align="center">
  <h3 align="center">IPCSD</h3>

  <p align="center">
    Esqueleto de proyecto para el laboratorio de IPCSD
    <br />
    <br />
    <a href="https://github.com/ppinedar/epcsd-spring-2023/issues">Report Bug</a>
    ·
    <a href="https://github.com/ppinedar/epcsd-spring-2023/issues">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Contenidos</summary>
  <ol>
    <li>
      <a href="#sobre-este-proyecto">Sobre este proyecto</a>
      <ul>
        <li><a href="#hecho-con">Hecho con</a></li>
      </ul>
    </li>
    <li>
      <a href="#antes-de-empezar">Antes de empezar</a>
    </li>
    <li>
      <a href="#instalación">Instalación</a>
      <ul>
        <li><a href="#docker-desktop--docker-compose">Docker Desktop / Docker Compose</a></li>
        <li><a href="#infraestructura-básica-dockers">Infraestructura básica (dockers)</a></li>
        <li><a href="#esqueletos-de-microservicios">Esqueletos de microservicios</a></li>
      </ul>
    </li>
    <li><a href="#enlaces-a-herramientas-librerías-y-módulos-usados">Enlaces a herramientas, librerías y módulos usados</a></li>
    <li><a href="#contacto">Contacto</a></li>
  </ol>
</details>

<!-- About this project -->
## Sobre este proyecto

Este es el proyecto de laboratorio de la asignatura EPCSD de la UOC. Se compone de 3 elementos (cada una tiene su repositorio GIT):

* Un archivo <a href="https://github.com/ppinedar/epcsd-spring-2023/blob/main/docker-compose.yml">docker-compose.yml</a> para arrancar la infraestructura básica necesaria para poder ejecutar los servicios
* Una carpeta para el microservicio <a href="https://github.com/ppinedar/epcsd-spring-2023-productcatalog">ProductCatalog</a>
* Una carpeta para el microservicio <a href="https://github.com/ppinedar/epcsd-spring-2023-user">User</a>
* Una carpeta para el microservicio <a href="https://github.com/ppinedar/epcsd-spring-2023-notification">Notification</a>

<p align="right">(<a href="#top">ir arriba</a>)</p>


### Hecho con

* [Docker](https://www.docker.com/) / [Docker Compose](https://github.com/docker/compose)
* [Spring](https://spring.io/) / [Spring Boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)
* [Apache Kafka](https://kafka.apache.org/)
* [PostgreSQL](https://www.postgresql.org/)

<p align="right">(<a href="#top">ir arriba</a>)</p>


## Antes de empezar

Para el funcionamiento de los contenedores que forman parte de la infraestructura básica del proyecto, se usaran los siguientes puertos:

* 22181 - Apache Kafka (Zookeeper)
* 19092 - Apache Kafka (Server)
* 54320, 54321 - PostgreSQL
* 18080 - Adminer
* 18081 - Usado por el microservicio productcatalog
* 18082 - Usado por el microservicio user

Para evitar conflictos con otras aplicaciones instaladas, se han modificado los puertos por defecto de todas las aplicaciones. Aún así, si hubiera un conflicto por un puerto ya en uso, bastaría con modificar los puertos especificados en el archivo [docker-compose.yml](https://github.com/ppinedar/epcsd-spring-2023/blob/main/docker-compose.yml) para solucionar el problema. Este link de la documentación oficial de docker compose explica como modificar esta configuración mediante la opción _ports_: [Networking in Compose](https://docs.docker.com/compose/networking/).

__AVISO IMPORTANTE:__ Los puertos que se modifiquen se tendran que cambiar también en la configuración de los microservicios (normalmente definidos en  el archivo _application.properties_).


## Instalación

### Docker Desktop / Docker Compose

Instalaremos Docker Compose siguiendo los pasos descritos (según SO) en la siguiente guía: https://docs.docker.com/compose/install/

Bajo Windows, es posible que sea necesario registrarse, ya que <a href="https://docs.docker.com/desktop/windows/install/">Docker Desktop</a> lo exige así para proyectos educativos/personales/no-comerciales. Como contrapartida, no será necesario instalar nada más porque ya incorpora _Compose_.

Es importante que reviséis con atención los requisitos de hardware y software descritos en las guías de instalación, puesto que si vuestro sistema no los cumple, aunque la aplicación se instale satisfactoriamente, dará errores al intentar arrancar contenedores. Una alternativa para aquellos que tengan sistemas un poco más antiguos es <a href="https://www.how2shout.com/how-to/how-to-install-docker-toolbox-using-chocolatey-choco-on-windows-10.html">Docker Toolbox</a>.

Una vez instalado Docker Compose, seguiremos con el esqueleto de proyecto. Se recomienda seguir la siguiente estructura de carpetas:

```
epcsd-spring-2023
├ README.md
├ docker-compose.yml
├ epcsd-spring-2023-notification
├ epcsd-spring-2023-productcatalog
└ epcsd-spring-2023-user
```

<p align="right">(<a href="#top">ir arriba</a>)</p>


### Infraestructura básica (dockers)

* Descargar ZIP / Clonar el repositorio <a href="https://github.com/ppinedar/epcsd-spring-2023">epcsd-spring-2023</a> en la carpeta de trabajo (_epcsd-spring-2023_ si se ha seguido la recomendación).

* Desde la carpeta, ejecutar el comando:

  ```sh
  docker compose up
  (Win)
  ```
  ```sh
  docker-compose up
  (Linux)
  ```

Deberían arrancarse los contenedores:

* epcsd-spring-2023-adminer_1 - adminer, un cliente SQL
* epcsd-spring-2023-kafka_1 - el servidor de kafka
* epcsd-spring-2023-productdb_1 - la bbdd postgresql correspondiente al servicio productcatalog
* epcsd-spring-2023-userdb_1 - la bbdd postgresql correspondiente al servicio user
* epcsd-spring-2023-zookeeper_1 - kafka zookeeper

Para verificar que se han levantado todos de manera correcta, ejecutaremos el siguiente comando:
  
  ```sh
  docker ps -a
  ```

Deberíamos ver algo como esto:

![Docker containers running](https://github.com/ppinedar/epcsd-spring-2023/blob/main/docker__containers_running.PNG)

Para comprobar el funcionamiento, se puede acceder al panel _Adminer_ en http://localhost:18080/ y hacer alguna consulta contra las BBDDs PostgreSQL que acabamos de instanciar con los siguientes datos de conexión:

* productdb
* Motor: PostgreSQL
* Servidor: productdb
* Usuario: product
* Contraseña: product
* Esquema: product

![Adminer productdb 1](https://github.com/ppinedar/epcsd-spring-2023/blob/main/adminer1.PNG)

![Adminer productdb 2](https://github.com/ppinedar/epcsd-spring-2023/blob/main/adminer1_1.PNG)

* userdb
* Motor: PostgreSQL
* Servidor: userdb
* Usuario: user
* Contraseña: user
* Esquema: user

![Adminer userdb 1](https://github.com/ppinedar/epcsd-spring-2023/blob/main/adminer2.PNG)

![Adminer userdb 2](https://github.com/ppinedar/epcsd-spring-2023/blob/main/adminer2_1.PNG)

### Esqueletos de microservicios

* Descargar ZIP / Clonar los repositorios <a href="https://github.com/ppinedar/epcsd-spring-2023-productcatalog">epcsd-spring-2023-productcatalog</a>, <a href="https://github.com/ppinedar/epcsd-spring-2023-user">epcsd-spring-2023-user</a> y <a href="https://github.com/ppinedar/epcsd-spring-2023-notification">epcsd-spring-2023-notification</a> dentro de la carpeta de trabajo (_epcsd-spring-2023_ si se ha seguido la recomendación)
* Abrir los proyectos en el entorno de desarrollo preferido
* Verificar la correcta compilación y ejecución arrancando los proyectos y comprobando que se puede acceder a http://localhost:18081/swagger-ui/index.html y http://localhost:18082/swagger-ui/index.html

<p align="right">(<a href="#top">ir arriba</a>)</p>


## Enlaces a herramientas, librerías y módulos usados

* [Docker](https://www.docker.com/) / [Docker Compose](https://github.com/docker/compose)
* [Spring](https://spring.io/) / [Spring Boot](https://spring.io/projects/spring-boot)
  * [spring-data-jpa](https://spring.io/projects/spring-data-jpa)
  * [spring-data-jdbc](https://spring.io/projects/spring-data-jdbc)
  * [spring-kafka](https://spring.io/projects/spring-kafka)
* [Apache Kafka](https://kafka.apache.org/)
* [PostgreSQL](https://www.postgresql.org/)
* [Lombok](https://projectlombok.org/)
* [springdoc-openapi-ui (SwaggerUI for OpenApi 3)](https://github.com/springdoc/springdoc-openapi)


## Contacto

Pau Pineda - ppineda0@uoc.edu

<p align="right">(<a href="#top">ir arriba</a>)</p>

