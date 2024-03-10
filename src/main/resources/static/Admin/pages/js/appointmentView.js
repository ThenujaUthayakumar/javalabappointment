$(document).ready(function() {
    var currentPage = 0;
    var pageSize = 10;
    var totalPages = 0;
    var appointmentsData = [];

    function fetchAppointments(searchKeyword) {
        $.ajax({
            url: 'http://localhost:8080/appointment',
            method: 'GET',
            dataType: 'json',
            data: {
                search: searchKeyword,
                sort: 'created_at,DESC'
            },
            success: function(data) {
                console.log('Data received:', data);
                if (Array.isArray(data.content)) {
                    appointmentsData = data.content;
                    renderAppointments(currentPage);
                } else {
                    console.error('Data is not in the expected format:', data);
                }
            },
            error: function(xhr, status, error) {
                console.error('Error fetching data from API:', error);
            }
        });
    }

    function renderAppointments(page) {
        currentPage = page;

        var startIndex = page * pageSize;
        var endIndex = startIndex + pageSize;
        var appointmentsToShow = appointmentsData.slice(startIndex, endIndex);
        $('#appointmentTable tbody').empty();
        appointmentsToShow.forEach(function(record) {
            $.ajax({
                url: 'http://localhost:8080/test?id=' + record.testId.id,
                method: 'GET',
                dataType: 'json',
                success: function(testData) {
                    console.log('Test data received:', testData);
                    var testName = testData.content[0].name;
                    var newRow = $('<tr>');
                    newRow.append('<td>' + record.referenceNumber + '</td>');
                    newRow.append('<td>' + record.name + '</td>');
                    newRow.append('<td>' + record.phoneNumber + '</td>');
                    newRow.append('<td>' + record.address + '</td>');
                    newRow.append('<td>' + record.email + '</td>');
                    newRow.append('<td>' + record.gender + '</td>');
                    newRow.append('<td>' + record.age + '</td>');
                    newRow.append('<td>' + testName + '</td>');
                    newRow.append('<td>' + record.appointmentDateTime + '</td>');
                    newRow.append('<td>' + record.doctorName + '</td>');
                    newRow.append('<td><i class="mdi mdi-trash-can-outline delete-icon" style="color:red;" data-appointment-id="' + record.id + '"></i></td>');
                    $('#appointmentTable tbody').append(newRow);
                },
                error: function(xhr, status, error) {
                    console.error('Error fetching test data:', error);
                }
            });
        });

        totalPages = Math.ceil(appointmentsData.length / pageSize);
        updatePagination();
    }

    $('#pagination').on('click', 'a.page-link', function(e) {
        e.preventDefault();
        var page = $(this).data('page');
        renderAppointments(page);
    });

    function updatePagination() {
        var paginationHtml = '';
        paginationHtml += '<li class="page-item ' + (currentPage === 0 ? 'disabled' : '') + '"><a class="page-link" href="#" data-page="' + (currentPage - 1) + '">Previous</a></li>';
        for (var i = 0; i < totalPages; i++) {
            paginationHtml += '<li class="page-item ' + (currentPage === i ? 'active' : '') + '"><a class="page-link" href="#" data-page="' + i + '">' + (i + 1) + '</a></li>';
        }
        paginationHtml += '<li class="page-item ' + (currentPage === totalPages - 1 ? 'disabled' : '') + '"><a class="page-link" href="#" data-page="' + (currentPage + 1) + '">Next</a></li>';
        $('#pagination').html(paginationHtml);
    }

    fetchAppointments('');

    $('#searchInput').on('input', function() {
        var searchKeyword = $(this).val();
        fetchAppointments(searchKeyword);
    });

    function showDeleteMessage(message, type) {
        var alertClass = 'alert-' + type;
        var alertMessage = '<div class="alert ' + alertClass + ' alert-dismissible fade show" role="alert">' +
            message +
            '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
            '</div>';
        $('#deleteMessage').html(alertMessage);
    }

    $('#appointmentTable').on('click', '.delete-icon', function() {
        var appointmentId = $(this).data('appointment-id');
        var row = $(this).closest('tr');
        $.ajax({
            url: 'http://localhost:8080/appointment/delete?id=' + appointmentId,
            method: 'DELETE',
            success: function(response) {
                console.log('Appointment deleted successfully:', response);
                row.remove();
                showDeleteMessage(response, 'success');
            },
            error: function(xhr, status, error) {
                console.error('Error deleting appointment:', error);
                showDeleteMessage('Error deleting appointment', 'danger');
            }
        });
    });

    $('#appointmentDownload').click(function(e) {
        e.preventDefault();
        var searchKeyword = $('#searchInput').val();
        var downloadUrl = 'http://localhost:8080/appointment/download?fileType=1';
        if (searchKeyword) {
            downloadUrl += '&search=' + encodeURIComponent(searchKeyword);
        }
        var iframe = $('<iframe>', {
            id: 'downloadFrame',
            src: downloadUrl,
            style: 'display: none;'
        });
        $('body').append(iframe);
    });
});
