var syncButton = document.getElementById('setup-calendar-connection');
syncButton.addEventListener('click', function () {
    fetch("/oauth2/google/auth-url")
        .then(response => response.json())
        .then(data => window.location.href = data.authUrl)
        .catch(err => console.log(err));
})


// При нажатии на кнопку, отправляем GET запрос на /calendars, получаем список календарей и отображаем их в модальном окне
const selectCalendarButton = document.getElementById("selectCalendarButton");
const calendarSelect = document.getElementById("calendarSelect");


document.getElementById("getCalendarList").addEventListener("click", function() {
    fetch("/callback/calendars")
        .then(response => response.json())

        .then(calendars => {
            console.log(calendars)
            calendarSelect.innerHTML = "";
            calendars.forEach(calendar => {
                const option = document.createElement("option");
                option.value = calendar.id;
                option.text = calendar.summary;
                calendarSelect.add(option);
            });
            $('#calendarModal').modal('show');
        })
        .catch(error => console.error(error));
});

selectCalendarButton.addEventListener("click", function() {
    const selectedCalendarId = calendarSelect.value;
    modal.style.display = 'none';
    console.log(selectedCalendarId);
    // делаем что-то с выбранным календарем
});

// Отправка события Post на календарь
const form = document.getElementById('eventForm');
form.addEventListener('submit', async (event) => {
    event.preventDefault();


    const formData = new FormData(form);
    const eventData = {
        summary: formData.get('summary'),
        description: formData.get('description'),
       //  start: { dateTime: formData.get('start') }

        // end: { dateTime: formData.get('end') },
    };

    // Send a POST request to the API endpoint
    const calendarId = 'primary'; // Replace with the actual calendar ID
    const response = await fetch(`callback/calendars/${calendarId}/events`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(eventData),
    });

    // Check the response status and display a message
    if (response.ok) {
        alert('Event added to calendar!');
    } else {
        alert('Error adding event to calendar!');
    }
});
