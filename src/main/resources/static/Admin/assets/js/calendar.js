document.addEventListener('DOMContentLoaded', function () {
    const calendarEl = document.getElementById('calendar');

    const calendar = new FullCalendar.Calendar(calendarEl, {
        plugins: ['dayGrid'],
        events: function (fetchInfo, successCallback, failureCallback) {
            $.ajax({
                url: 'http://localhost:8080/appointment',
                method: 'GET',
                success: function (response) {
                    const appointments = response.content;
                    const formattedEvents = appointments.map(appointment => ({
                        title: appointment.name,
                        start: appointment.appointmentDateTime
                    }));

                    successCallback(formattedEvents);
                },
                error: function (xhr, status, error) {
                    failureCallback(error);
                }
            });
        },
        eventClick: function (info) {
            alert('Appointment: ' + info.event.title);
        }
    });

    calendar.render();
});