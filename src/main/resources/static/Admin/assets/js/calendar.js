document.addEventListener('DOMContentLoaded', function() {
  var calendarEl = document.getElementById('calendar');

  var calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: 'dayGridMonth', // Set initial view to month
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,timeGridDay'
    },
    events: {
      url: 'http://example.com/api/events', // Replace with your API endpoint
      method: 'GET',
      failure: function() {
        console.error('Error fetching events from the API');
      }
    }
  });

  calendar.render();
});
