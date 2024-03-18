$(document).ready(function() {
    var currentPage = 0;
    var pageSize = 10;
    var totalPages = 0;
    var contactsData = [];

    function fetchContacts(searchKeyword) {
        $.ajax({
            url: 'http://localhost:8080/report?orderBy=created_at,DESC',
            method: 'GET',
            dataType: 'json',
            data: {
                search: searchKeyword,
                sort: 'created_at,DESC'
            },
            success: function(data) {
                console.log('Data received:', data);
                if (Array.isArray(data.content)) {
                    contactsData = data.content;
                    renderContacts(currentPage);
                } else {
                    console.error('Data is not in the expected format:', data);
                }
            },
            error: function(xhr, status, error) {
                console.error('Error fetching data from API:', error);
            }
        });
    }

    function renderContacts(page) {
        currentPage = page;

        var startIndex = page * pageSize;
        var endIndex = startIndex + pageSize;
        var contactsToShow = contactsData.slice(startIndex, endIndex);
        $('#reportTable tbody').empty();
        contactsToShow.forEach(function(record) {
            var newRow = $('<tr>');
            newRow.append('<td>' + record.labNo + '</td>');
            newRow.append('<td>' + record.technician + '</td>');
            newRow.append('<td>' + record.patientNo + '</td>');
            newRow.append('<td>' + record.patientName + '</td>');
            newRow.append('<td>' + record.patientMobile + '</td>');
            newRow.append('<td>' + record.test + '</td>');
            var createdAt = new Date(record.date);
            var formattedCreatedAt = createdAt.getFullYear() + '-' +
                ('0' + (createdAt.getMonth() + 1)).slice(-2) + '-' +
                ('0' + createdAt.getDate()).slice(-2) + ' ' +
                ('0' + createdAt.getHours()).slice(-2) + ':' +
                ('0' + createdAt.getMinutes()).slice(-2) + ':' +
                ('0' + createdAt.getSeconds()).slice(-2);
            newRow.append('<td>' + formattedCreatedAt + '</td>');
            const pdfPath = JSON.parse(record.file).filePath;
            const imageUrl = 'http://localhost:8080/report/' + encodeURIComponent(pdfPath);
            newRow.append('<td><i class="mdi mdi-eye show-image-icon" style="color:blue;" data-file-url="' + imageUrl + '"></i></td>');
            var statusText;
            if (record.status === 1) {
                statusText = 'Completed';
                newRow.append('<td><label class="badge badge-success">' + statusText + '</label></td>');
            } else if (record.status === 0) {
                statusText = 'Pending';
                newRow.append('<td><label class="badge badge-warning">' + statusText + '</label></td>');
            }
            newRow.append('<td>' +
                '<a href="./create_codes/updateReport.html?id=' + record.id + '"><i class="mdi mdi-pencil-outline edit-icon" style="color:blue;"></i></a>' +
                '<i class="mdi mdi-share-variant share-icon" style="color:green;" data-contact-id="' + record.id + '"></i>' +
                '<i class="mdi mdi-trash-can-outline delete-icon" style="color:red;" data-contact-id="' + record.id + '"></i>' +
                '</td>');

            $('#reportTable tbody').append(newRow);
        });

            $(document).on('click', '.show-image-icon', function() {
                var pdfUrl = $(this).data('file-url');
                if (pdfUrl) {
                    showImage(pdfUrl);
                }
            });
            function showImage(pdfUrl) {
                var modalContent = '<div class="modal fade" id="pdfModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">' +
                    '<div class="modal-dialog modal-lg">' +
                    '<div class="modal-content">' +
                    '<div class="modal-header">' +
                    '<h5 class="modal-title" id="exampleModalLabel">Lab Report</h5>' +
                    '<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>' +
                    '</div>' +
                    '<div class="modal-body">' +
                    '<iframe src="' + pdfUrl + '" class="pdf-viewer" width="100%" height="600px" allowfullscreen></iframe>' + // Set width, height, and allowfullscreen
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>';

                $('body').append(modalContent);
                $('#pdfModal').modal('show');
                $('#pdfModal').on('hidden.bs.modal', function () {
                    $(this).remove();
                });
            }


        totalPages = Math.ceil(contactsData.length / pageSize);
        updatePagination();
    }

    $('#pagination').on('click', 'a.page-link', function(e) {
        e.preventDefault();
        var page = $(this).data('page');
        renderContacts(page);
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

    fetchContacts('');

    $('#searchInput').on('input', function() {
        var searchKeyword = $(this).val();
        fetchContacts(searchKeyword);
    });

    function showDeleteMessage(message, type) {
        var alertClass = 'alert-' + type;
        var alertMessage = '<div class="alert ' + alertClass + ' alert-dismissible fade show" role="alert">' +
            message +
            '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
            '</div>';
        $('#deleteMessage').html(alertMessage);
    }

    $(document).on('click', '.delete-icon', function() {
        var contactId = $(this).data('contact-id');
        var row = $(this).closest('tr');
        $.ajax({
            url: 'http://localhost:8080/report/delete?id=' + contactId,
            method: 'DELETE',
            success: function(response) {
                console.log('contact deleted successfully:', response);
                row.remove();
                showDeleteMessage(response, 'success');
            },
            error: function(xhr, status, error) {
                console.error('Error deleting contact:', error);
                showDeleteMessage('Error deleting contact', 'danger');
            }
        });
    });

    $('#reportDownload').click(function(e) {
        e.preventDefault();
        var searchKeyword = $('#searchInput').val();
        var downloadUrl = 'http://localhost:8080/report/download?fileType=1';
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
