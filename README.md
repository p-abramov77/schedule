# Redirector

Redirector is a web service that takes url query parameter, decodes it, and does a 302 redirect

Сервис предоставляет переадресацию по зарегистрированным коротким URL при обращении в виде:

```
http://<server-address>:<port>/redir/<короткий URL>
```

### Документация API (OpenApi)

Для получения справки по API в браузере требуется набрать адрес:

```
http://<server-address>:<port>/docs
```

### Администрирование редиректов

Для доступа к админке требуется набрать адрес:

```
http://<server-address>:<port>/active
```
### Проверка доступности сервиса

Для проверки доступности серсиса следует запросить страницу:
```
http://<server-address>:<port>/actuator/health
```
## Запуск unit-тестов
Находясь в корневой директории проекта (redir) выполнить команду
```
./gradlew test
```
Детализированный результат тестов будет сохранен в redir/build/reports/tests/test/index.html

## Создание исполняемого jar-файла
Находясь в корневой директории проекта (redir) выполнить команду
```
./gradlew clean bootJar -Pversion={version}
```
Где {version} - требуемая версия файла, подставить значение.
Либо, с предварительным запуском всех тестов
```
./gradlew clean test bootJar -Pversion={version}
```
В случае успешного выполнения команды в директории ./redir/build/libs появится исполняемый rbcmetr-chekak-impl-{version}.jar

## Запуск исполняемого jar-файла
Запуск осуществляется командой java -jar redir-{version}.jar
Должен быть установлен **JDK_11**.

## Конфигуация
Для задания нижеописанных конфигурационных параметров потребуется задать переменные окружения с соответствующими именами, либо будут использоваться значения по умолчанию.

### Хранилище (Postgres)
**SPRING_DATASOURCE_JDBC_URL** - connection string для БД

Пример значения:
```
jdbc:postgresql://${SQL_HOST:localhost}:5432/default
```

**SPRING_DATASOURCE_USERNAME** - имя пользователя для БД

Пример значения:
```
dbuser1
```

**SPRING_DATASOURCE_PASSWORD** - пароль пользователя для БД

Пример значения:
```
password
```


