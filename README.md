# tkur_jacoco 0.8.15

**tkur_jacoco — проект ООО «ТЕХКОНСУР» для облегчения проведения
сертификационных испытаний ФСТЭК России.**

Форк [JaCoCo 0.8.15](https://github.com/jacoco/jacoco/releases/tag/v0.8.15)
с фирменным оформлением HTML-отчётов. Это не заявление о сертификации
инструмента или автоматическом соответствии требованиям ФСТЭК России.

## Отличия от JaCoCo

- Шапка и подвал «Испытательная лаборатория ООО "ТЕХКОНСУР"» на всех страницах.
- Синие таблицы, навигация, оформление исходников, адаптация к узкому экрану
  и печати. Широкие таблицы прокручиваются горизонтально.
- Палитра [официального сайта](https://tkur.ru/): `#021D59`, `#0A42BE`,
  `#1A9AE2`, `#F2F5FD`. В шапку встроен предоставленный SVG-логотип `tkur.svg`.
- Зелёный, красный и жёлтый сохраняют смысл покрытия. Есть русская легенда.
- Все ресурсы локальные: просмотр HTML не требует интернета.

Инструментация, формат `.exec`, расчёт метрик, XML/CSV и команды CLI не
изменены. Интерфейс HTML переведён на русский: колонки, навигация, подсказки,
сообщения и сведения о сессиях. Имена классов, методов и исходный код не переводятся.
Изменения находятся
в модуле `org.jacoco.report`, поэтому применяются и к встроенному HTMLFormatter.

Основа: upstream commit `6c5260a192eaa535e4a519771d530781cbac9136`.
Редакция форка: `0.8.15-tkur.2`. Внутренние версии и Maven-координаты upstream
сохранены ради совместимости; `version` CLI показывает JaCoCo 0.8.15.
Не публикуйте эти сборки под координатами оригинала в Maven Central.

## Сборка

Нужны JDK 21+ и доступ к Maven Central при первой сборке. Maven 3.9.16
скачивается wrapper-скриптом с проверкой SHA-256.

```bash
JAVA_HOME=/path/to/jdk-21 bash scripts/build-tkur.sh
```

Скрипт запускает тесты report/CLI, собирает артефакты в `target/dist/`:

- `tkur_jacococli.jar` — самостоятельный CLI со всеми зависимостями;
- `tkur_jacocoagent.jar` — агент сбора покрытия (логика upstream без изменений);
- `tkur_jacoco_report.jar` — библиотека генерации отчётов, не standalone;
- `tkur_jacoco_report-sources.jar` — исходники библиотеки;
- `SHA256SUMS`, `README.md`, `LICENSE.md`.

Не запускайте `mvn install` в общем локальном репозитории, если не хотите
заменить стандартные артефакты JaCoCo той же версии. Для интеграции Maven/Ant
используйте изолированный репозиторий либо готовый CLI.

## Использование

Можно использовать уже имеющийся файл покрытия, в том числе из Jazzer:

```bash
java -jar target/dist/tkur_jacococli.jar report coverage.exec \
  --classfiles application.jar \
  --sourcefiles src/main/java \
  --name 'Отчёт об испытаниях' \
  --html report \
  --xml report.xml
```

Открыть `report/index.html`. Классы должны соответствовать тем, на которых
собиралось покрытие. Исходники нужны для построчной навигации, но не для метрик.
`--classfiles` можно повторять для нескольких JAR или каталогов классов.

Сбор покрытия обычного Java-приложения:

```bash
java -javaagent:target/dist/tkur_jacocoagent.jar=destfile=coverage.exec -jar application.jar
```

## Проверки

Проверены 297 тестов генератора отчётов и 33 теста CLI. На Apache Commons
Compress 1.27.1 (486 классов, покрытие из tkur_jazzer) XML и CSV побайтно
совпали с оригинальным JaCoCo 0.8.15. В Chromium проверены сортировка,
переходы к исходникам, сессии, локальные ресурсы, узкий экран и печать.

## Лицензия и upstream

Сохранена [Eclipse Public License 2.0](LICENSE.md), исходная история Git
и уведомления об авторстве. Изменения форка также распространяются по EPL-2.0.
JaCoCo — проект Mountainminds GmbH & Co. KG и участников upstream.

---

## JaCoCo Java Code Coverage Library (upstream)

[![Build Status](https://dev.azure.com/jacoco-org/JaCoCo/_apis/build/status/JaCoCo?branchName=master)](https://dev.azure.com/jacoco-org/JaCoCo/_build/latest?definitionId=1&branchName=master)
[![Maven Central](https://img.shields.io/maven-central/v/org.jacoco/jacoco.svg)](https://central.sonatype.com/namespace/org.jacoco)

JaCoCo is a free Java code coverage library distributed under the Eclipse Public
License.

## Starting Points

*   I want to use JaCoCo → [Download](https://www.jacoco.org/jacoco/), [Maven](https://www.jacoco.org/jacoco/trunk/doc/maven.html), [Ant](https://www.jacoco.org/jacoco/trunk/doc/ant.html), [CLI](https://www.jacoco.org/jacoco/trunk/doc/cli.html), [Other](https://www.jacoco.org/jacoco/trunk/doc/integrations.html)
*   I want to know how JaCoCo works → [Documentation](http://www.jacoco.org/jacoco/trunk/doc/)
*   I have a question → [FAQ](http://www.jacoco.org/jacoco/trunk/doc/faq.html), [Documentation](http://www.jacoco.org/jacoco/trunk/doc/), [User Forum](https://groups.google.com/forum/?fromgroups=#!forum/jacoco)
*   I found a bug → [Bug Report](https://github.com/jacoco/jacoco/issues/new/choose)
*   I have an idea → [User Forum](https://groups.google.com/forum/?fromgroups=#!forum/jacoco), [Feature Request](https://github.com/jacoco/jacoco/issues/new/choose)
