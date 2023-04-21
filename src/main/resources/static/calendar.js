var syncButton = document.getElementById('setup-calendar-connection');
syncButton.addEventListener('click', function () {
    fetch("/oauth2/google/auth-url")
        .then(response => response.json())
        .then(data => window.location.href = data.authUrl)
        .catch(err => console.log(err));
})

// document.getElementById("getCalendarList").addEventListener("click", function() {
//     fetch("/callback/calendars")
//         .then(function(response) {
//             console.log(response);
//             return response.json();
//
//         })
//         .then(function(calendars) {
//             var modalBody = document.getElementById('calendarModal');
//
//             modalBody.innerHTML = "";
//             calendars.forEach(function(calendar) {
//                 var radio = document.createElement("input");
//                 radio.type = "radio";
//                 radio.name = "calendar";
//                 radio.value = calendar.id;
//                 modalBody.appendChild(radio);
//
//                 var label = document.createElement("label");
//                 label.innerHTML = calendar.summary;
//                 modalBody.appendChild(label);
//
//                 modalBody.appendChild(document.createElement("br"));
//             });
//
//             $("#calendarModal").modal("show");
//         });
// });
// При нажатии на кнопку, отправляем GET запрос на /calendars, получаем список календарей и отображаем их в модальном окне
const selectCalendarButton = document.getElementById("getCalendarList");
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
    // делаем что-то с выбранным календарем
});


// let btnList = document.getElementById('getCalendarList');
// btnList.addEventListener('click', function() {
//   console.log("клик")
//     fetch("/callback/calendars")
//         .then(response=>response.json())
//
// })

// $(function () {
//     $('#getCalendarList').click(function () {
//
//         $.get("/callback/calendars", function (calendars) {
//             var modalBody = $("#calendarModal.modal-body");
//             modalBody.empty();
//             $.each(calendars, function (i, calendar) {
//                 modalBody.append("<div><input type='radio' name='calendar' value='" + calendar.id + "'> " + calendar.summary + "</div>");
//             });
//             $("#calendarModal").modal("show");
//         });
//
//     })
// })




// $(function() {
//     $('getCalendarList').click(function() {
//         console.log("click")
//         $.get("/callback/calendars", function(calendars) {
//             var modalBody = $("#calendarModal.modal-body");
//             modalBody.empty();
//             $.each(calendars, function(i, calendar) {
//                 modalBody.append("<div><input type='radio' name='calendar' value='" + calendar.id + "'> " + calendar.summary + "</div>");
//             });
//             $("#calendarModal").modal("show");
//         });
//     });
// });
