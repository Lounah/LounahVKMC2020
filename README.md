# VK Mobile Challenge 2020

## Фича на финал – PLACES

Лежит в feature-places и имеет свой README

## Что было сделано

<li>Фото/видео пикер/обработчик -- создание альбомов, загрузка и удаление фотографий, удаление альбомов</li>
<li>Шаринг контента -- загрузка фотографий при помощи кастомного пикера и шаринг на стену</li>
<li>Интерфейс для отписки от сообществ</li>
<li>Товары</li>
<li>Карта -- отображение ивентов, групп и фотографий по геопозиции</li>

## Структура проекта
В develop ветке была написана кора (кор со стилями, общими компонентами, кор для работы с vk api). От этой ветки я бранчевался, реализовывая все фичи. Не успел повыпиливать ненужные фича-модули для каждой таски, поэтому получилось так, как получилось :(

## Архитектура
Мною был использована MVI-like архитектура, основанная на ReduxStore Дорфманна, я решил поэксперементировать. MVI-like она потому,
что одним из ее главных недостатков является обилие операторов с сайд-эффектами (.doOnNext, .doOnError, .doOnSuccess) -- из-за этого
ни о каком однонаправленном потоке данных речи быть не может. В остальном она показала себя норм, разве что напрягает, что сайд-эффекты нельзя выносить из презентера, из-за чего на больших проектах презентеры могут разрастаться до 300+ строк, а не просто состоять из набора функций

## DI
Была взята структура даггера с модулями и компонентами и с помощью делегатов было сделано примерно то же, что сам даггер генерит, только писалось это руками. По-моему, норм подход, который поддерживает все основные фичи даггера (например, скоупы, провайдеры, лэйзи, синглтоны)

## Core-recycler
Здесь представлено две обертки для работы с ресайклером. Одна из них оказалась ненужной и используется только в feature-image-picker, выпилить эту обертку я не успел :(

## Core-vk
Нет обработки ошибки, когда vk отдает User authorization failed. Не прикрутил интерсептор и не успел запихнуть в errorHandler rxJava

## Использованные библиотеки
<li>RxJava, RxAndroid, RxKotlin</li>
<li>RxRelay</li>
<li>ReduxStore</li>
<li>Glide</li>
<li>Gson</li>
<li>GoogleMaps + GoogleMapsUtil (0.6.2, а не недавно вышедший 1.0.0, потому что 1.0.0 показался мне нестабильным)</li>
<li>EasyPermissions</li>

## TBD

<li>Избавиться от ненужных модулей</li>
<li>В некоторых модулях присутствуют классы-копипаста из других модулей -- не ок</li>
<li>Локализация</li>

