$(document).ready(function() {
    function fetchTotalAppointments() {
        $.ajax({
            url: 'http://localhost:8080/appointment/statistics',
            method: 'GET',
            dataType: 'json',
            success: function(data) {
                var totalAppointments = data.content[0].totalAppointments;
                $('#totalAppointments').text(totalAppointments);
            },
            error: function(xhr, status, error) {
                console.error('Error fetching total appointments:', error);
            }
        });
    }

        function fetchTotalIncomes() {
            $.ajax({
                url: 'http://localhost:8080/payment/statistics',
                method: 'GET',
                dataType: 'json',
                success: function(data) {
                    var totalIncomes = data.content[0].totalIncomes;
                    $('#totalIncomes').text("රු "+totalIncomes);
                },
                error: function(xhr, status, error) {
                    console.error('Error fetching total incomes:', error);
                }
            });
        }

    $(document).ready(function() {
        fetchTotalAppointments();
        fetchTotalIncomes();
    });
});
