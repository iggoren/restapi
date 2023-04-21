// Получаем кнопку, которая будет открывать модальное окно
const openModalBtn = document.getElementById('getCalendarList');

// Получаем модальное окно
const modal = document.getElementById('calendarModal');

// Получаем контейнер для списка календарей в модальном окне
const calendarListContainer = document.getElementById('modal-body');

// Добавляем обработчик события клика на кнопку открытия модального окна
openModalBtn.addEventListener('click', () => {
    // Отправляем GET-запрос на сервер
    fetch('/callback/calendars')
        .then(response => response.json())
        .then(calendars => {
            // Очищаем контейнер для списка календарей
            calendarListContainer.innerHTML = '';

            // Добавляем каждый календарь в контейнер
            calendars.forEach(calendar => {
                const calendarElement = document.createElement('div');
                calendarElement.textContent = calendar.summary;
                calendarListContainer.appendChild(calendarElement);
            });

            // Показываем модальное окно
            modal.style.display = 'block';
        })
        .catch(error => console.error(error));
});

// Добавляем обработчик события клика на кнопку закрытия модального окна
const closeModalBtn = document.getElementById('selectCalendarButton');
closeModalBtn.addEventListener('click', () => {
    // Скрываем модальное окно
    modal.style.display = 'none';
});
