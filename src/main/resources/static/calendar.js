



var syncButton = document.getElementById('googleBtn');
syncButton.addEventListener('click', function () {
    fetch("api/oauth/google/auth-url")
        .then(response => response.text())
        .then(authUrl => window.location.href = authUrl)
        .catch(err => console.log(err));
})

// При нажатии на кнопку, отправляем GET запрос на /calendars, получаем список календарей и отображаем их в модальном окне
const selectCalendarButton = document.getElementById("selectCalendarButton");
const calendarSelect = document.getElementById("calendarSelect");


document.getElementById("getCalendarList").addEventListener("click", function() {
    fetch("api/oauth/calendars")
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


    //modal.style.display = "none";

    // Send POST request to server
    fetch(`api/oauth/saveCalendarId?calendarId=${selectedCalendarId}`, {
        method: 'POST',

        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                console.log('Calendar ID saved successfully.');
                // Refresh the page
                location.reload();
            } else {
                console.error('Failed to save calendar ID.');
            }
        })
        .catch(error => console.error(error));
   // location.reload();
});

// Отправка события Post на календарь
const form = document.getElementById('eventForm');
form.addEventListener('submit', async (event) => {
    event.preventDefault();


    const formData = new FormData(form);
    const eventData = {
        summary: formData.get('summary'),
        description: formData.get('description'),
         start: { dateTime: formData.get('start') },

        end: { dateTime: formData.get('end') },
    };

    // Send a POST request to the API endpoint
   // const calendarId = 'primary'; // Replace with the actual calendar ID
    const response = await fetch(`api/oauth/calendars/events`, {
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
