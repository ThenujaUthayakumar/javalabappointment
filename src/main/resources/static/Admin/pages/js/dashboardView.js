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

                function fetchTotalContacts() {
                    $.ajax({
                        url: 'http://localhost:8080/contact/statistics',
                        method: 'GET',
                        dataType: 'json',
                        success: function(data) {
                            var totalContacts = data.content[0].totalContacts;
                            $('#totalContacts').text(totalContacts);
                        },
                        error: function(xhr, status, error) {
                            console.error('Error fetching total contact:', error);
                        }
                    });
                }

                                function fetchTotalTests() {
                                    $.ajax({
                                        url: 'http://localhost:8080/test/statistics',
                                        method: 'GET',
                                        dataType: 'json',
                                        success: function(data) {
                                            var totalTests = data.content[0].totalTests;
                                            $('#totalTests').text(totalTests);
                                        },
                                        error: function(xhr, status, error) {
                                            console.error('Error fetching total contact:', error);
                                        }
                                    });
                                }
                                                                function fetchTotalTechnicians() {
                                                                    $.ajax({
                                                                        url: 'http://localhost:8080/technician/statistics',
                                                                        method: 'GET',
                                                                        dataType: 'json',
                                                                        success: function(data) {
                                                                            var totalTechnicians = data.content[0].totalTechnicians;
                                                                            $('#totalTechnicians').text(totalTechnicians);
                                                                        },
                                                                        error: function(xhr, status, error) {
                                                                            console.error('Error fetching total contact:', error);
                                                                        }
                                                                    });
                                                                }

    $(document).ready(function() {
        fetchTotalAppointments();
        fetchTotalIncomes();
        fetchTotalContacts();
        fetchTotalTests();
        fetchTotalTechnicians();
    });
});
