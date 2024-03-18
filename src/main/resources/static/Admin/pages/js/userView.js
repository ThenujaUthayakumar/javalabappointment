$(document).ready(function() {
    var currentPage = 0;
    var pageSize = 10;
    var totalPages = 0;
    var usersData = [];

    function fetchTests(searchKeyword) {
        $.ajax({
            url: 'http://localhost:8080/user?orderBy=created_at,DESC',
            method: 'GET',
            dataType: 'json',
            data: {
                search: searchKeyword,
                sort: 'created_at,DESC'
            },
            success: function(data) {
                console.log('Data received:', data);
                if (Array.isArray(data.content)) {
                    usersData = data.content;
                    renderUsers(currentPage);
                } else {
                    console.error('Data is not in the expected format:', data);
                }
            },
            error: function(xhr, status, error) {
                console.error('Error fetching data from API:', error);
            }
        });
    }

    function renderUsers(page) {
        currentPage = page;

        var startIndex = page * pageSize;
        var endIndex = startIndex + pageSize;
        var usersToShow = usersData.slice(startIndex, endIndex);
        $('#userTable tbody').empty();
        usersToShow.forEach(function(record) {
            var newRow = $('<tr>');
            newRow.append('<td>' + record.name + '</td>');
            newRow.append('<td>' + record.address + '</td>');
            newRow.append('<td>' + record.phoneNumber + '</td>');
            newRow.append('<td>' + record.email + '</td>');
            newRow.append('<td>' + record.role + '</td>');
            newRow.append('<td>' + record.username + '</td>');
            newRow.append('<td><a href="./create_codes/updateUser.html?id=' + record.id + '"><i class="mdi mdi-pencil-outline edit-icon" style="color:blue;"></i></a><i class="mdi mdi-trash-can-outline delete-icon" style="color:red;" data-user-id="' + record.id + '"></i></td>');
            $('#userId').val(record.id);
            $('#userTable tbody').append(newRow);
        });

        totalPages = Math.ceil(usersData.length / pageSize);
        updatePagination();
    }

    $('#pagination').on('click', 'a.page-link', function(e) {
        e.preventDefault();
        var page = $(this).data('page');
        renderUsers(page);
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

    fetchTests('');

    $('#searchInput').on('input', function() {
        var searchKeyword = $(this).val();
        fetchTests(searchKeyword);
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
        var userId = $(this).data('user-id');
        var row = $(this).closest('tr');
        $.ajax({
            url: 'http://localhost:8080/user/delete?id=' + userId,
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

    $('#userDownload').click(function(e) {
        e.preventDefault();
        var searchKeyword = $('#searchInput').val();
        var downloadUrl = 'http://localhost:8080/user/download?fileType=1';
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
