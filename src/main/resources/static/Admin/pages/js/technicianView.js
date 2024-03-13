$(document).ready(function() {
    var currentPage = 0;
    var pageSize = 10;
    var totalPages = 0;
    var techniciansData = [];

    function fetchTechnicians(searchKeyword) {
        $.ajax({
            url: 'http://localhost:8080/technician?orderBy=created_at,DESC',
            method: 'GET',
            dataType: 'json',
            data: {
                search: searchKeyword,
                sort: 'created_at,DESC'
            },
            success: function(data) {
                console.log('Data received:', data);
                if (Array.isArray(data.content)) {
                    techniciansData = data.content;
                    renderTechnicians(currentPage);
                } else {
                    console.error('Data is not in the expected format:', data);
                }
            },
            error: function(xhr, status, error) {
                console.error('Error fetching data from API:', error);
            }
        });
    }

    function renderTechnicians(page) {
        currentPage = page;

        var startIndex = page * pageSize;
        var endIndex = startIndex + pageSize;
        var contactsToShow = techniciansData.slice(startIndex, endIndex);
        $('#technicianTable tbody').empty();
        contactsToShow.forEach(function(record) {
            var newRow = $('<tr>');
            newRow.append('<td>' + record.name + '</td>');
            newRow.append('<td>' + record.phoneNumber + '</td>');
            newRow.append('<td>' + record.address + '</td>');
            newRow.append('<td>' + record.email + '</td>');
            newRow.append('<td>' + record.position + '</td>');
            newRow.append('<td>' + record.joinDate + '</td>');
            const imagePath = JSON.parse(record.image).filePath;
            const imageUrl = 'http://localhost:8080/img/technician/' + imagePath;
            newRow.append('<td><i class="mdi mdi-eye show-image-icon" style="color:blue;" data-image-url="' + imageUrl + '"></i></td>');
            newRow.append('<td><i class="mdi mdi-trash-can-outline delete-icon" style="color:red;" data-contact-id="' + record.id + '"></i></td>');
            $('#technicianTable tbody').append(newRow);
        });
            $(document).on('click', '.show-image-icon', function() {
                var imageUrl = $(this).data('image-url');
                if (imageUrl) {
                    showImage(imageUrl);
                }
            });
            function showImage(imageUrl) {
                var modalContent = '<div class="modal fade" id="imageModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">' +
                                      '<div class="modal-dialog">' +
                                        '<div class="modal-content">' +
                                          '<div class="modal-header">' +
                                            '<h5 class="modal-title" id="exampleModalLabel">Technician Profile</h5>' +
                                            '<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>' +
                                          '</div>' +
                                          '<div class="modal-body">' +
                                            '<img src="' + imageUrl + '" class="img-fluid">' +
                                          '</div>' +
                                        '</div>' +
                                      '</div>' +
                                    '</div>';

                $('body').append(modalContent);
                $('#imageModal').modal('show');
                $('#imageModal').on('hidden.bs.modal', function () {
                    $(this).remove();
                });
            }
        totalPages = Math.ceil(techniciansData.length / pageSize);
        updatePagination();
    }

    $('#pagination').on('click', 'a.page-link', function(e) {
        e.preventDefault();
        var page = $(this).data('page');
        renderTechnicians(page);
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

    fetchTechnicians('');

    $('#searchInput').on('input', function() {
        var searchKeyword = $(this).val();
        fetchTechnicians(searchKeyword);
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
            url: 'http://localhost:8080/technician/delete?id=' + contactId,
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

    $('#technicianDownload').click(function(e) {
        e.preventDefault();
        var searchKeyword = $('#searchInput').val();
        var downloadUrl = 'http://localhost:8080/technician/download?fileType=1';
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
