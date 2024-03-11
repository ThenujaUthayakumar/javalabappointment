$(document).ready(function() {
    var currentPage = 0;
    var pageSize = 10;
    var totalPages = 0;
    var paymentsData = [];

    function fetchPayments(searchKeyword) {
        $.ajax({
            url: 'http://localhost:8080/payment?orderBy=created_at,DESC',
            method: 'GET',
            dataType: 'json',
            data: {
                search: searchKeyword,
                sort: 'created_at,DESC'
            },
            success: function(data) {
                console.log('Data received:', data);
                if (Array.isArray(data.content)) {
                    paymentsData = data.content;
                    renderPayments(currentPage);
                } else {
                    console.error('Data is not in the expected format:', data);
                }
            },
            error: function(xhr, status, error) {
                console.error('Error fetching data from API:', error);
            }
        });
    }

    function renderPayments(page) {
        currentPage = page;

        var startIndex = page * pageSize;
        var endIndex = startIndex + pageSize;
        var appointmentsToShow = paymentsData.slice(startIndex, endIndex);
        $('#paymentTable tbody').empty();
        appointmentsToShow.forEach(function(record) {
            $.ajax({
                url: 'http://localhost:8080/appointment?id=' + record.appointmentId.id,
                method: 'GET',
                dataType: 'json',
                success: function(testData) {
                    console.log('Test data received:', testData);
                    var appointmentReferenceNumber = testData.content[0].referenceNumber;
                    var appointmentName=testData.content[0].name;
                    var appointmentTest=testData.content[0].testId.name;
                    var newRow = $('<tr>');
                    newRow.append('<td>' + record.referenceNumber + '</td>');
                    newRow.append('<td>' + appointmentReferenceNumber + '</td>');
                    newRow.append('<td>' + appointmentName + '</td>');
                    newRow.append('<td>' + record.cardHolderName + '</td>');
                    newRow.append('<td>' + record.cardHolderPhoneNumber + '</td>');
                    newRow.append('<td>' + appointmentTest + '</td>');
                    newRow.append('<td>' + record.amount + '</td>');
                    var paymentDate = new Date(record.createdAt);
                    var formattedPaymentDate = paymentDate.getFullYear() + '-' +
                        ('0' + (paymentDate.getMonth() + 1)).slice(-2) + '-' +
                        ('0' + paymentDate.getDate()).slice(-2) + ' ' +
                        ('0' + paymentDate.getHours()).slice(-2) + ':' +
                        ('0' + paymentDate.getMinutes()).slice(-2);
                    newRow.append('<td>' + formattedPaymentDate + '</td>');
                    var statusText = record.status === 1 ? 'Paid' : record.status;
                    newRow.append('<td><label class="badge badge-success">' + statusText + '</label></td>');
                    newRow.append('<td><i class="mdi mdi-trash-can-outline delete-icon" style="color:red;" data-payment-id="' + record.id + '"></i></td>');
                    $('#paymentTable tbody').append(newRow);
                },
                error: function(xhr, status, error) {
                    console.error('Error fetching test data:', error);
                }
            });
        });

        totalPages = Math.ceil(paymentsData.length / pageSize);
        updatePagination();
    }

    $('#pagination').on('click', 'a.page-link', function(e) {
        e.preventDefault();
        var page = $(this).data('page');
        renderPayments(page);
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

    fetchPayments('');

    $('#searchInput').on('input', function() {
        var searchKeyword = $(this).val();
        fetchPayments(searchKeyword);
    });

    function showDeleteMessage(message, type) {
        var alertClass = 'alert-' + type;
        var alertMessage = '<div class="alert ' + alertClass + ' alert-dismissible fade show" role="alert">' +
            message +
            '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
            '</div>';
        $('#deleteMessage').html(alertMessage);
    }

    $('#paymentTable').on('click', '.delete-icon', function() {
        var paymentId = $(this).data('payment-id');
        var row = $(this).closest('tr');
        $.ajax({
            url: 'http://localhost:8080/payment/delete?id=' + paymentId,
            method: 'DELETE',
            success: function(response) {
                console.log('Payment deleted successfully:', response);
                row.remove();
                showDeleteMessage(response, 'success');
            },
            error: function(xhr, status, error) {
                console.error('Error deleting payment:', error);
                showDeleteMessage('Error deleting payment', 'danger');
            }
        });
    });

    $('#paymentDownload').click(function(e) {
        e.preventDefault();
        var searchKeyword = $('#searchInput').val();
        var downloadUrl = 'http://localhost:8080/payment/download?fileType=1';
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
