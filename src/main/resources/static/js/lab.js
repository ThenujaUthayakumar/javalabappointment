$(document).ready(function () {
    $('#reportForm').on('submit', function (e) {
        e.preventDefault();
        var search = $('#search').val();
        if (!search) {
            $('#errorContainer').show();
            $('#recordsTable').hide();
            $('#successMessage').hide();
            return;
        }

        $.ajax({
            url: 'http://localhost:8080/report',
            method: 'GET',
            data: { search: search },
            success: function (response) {
                if (response.content && response.content.length > 0) {
                    displayRecords(response.content);
                    $('#successMessage').show();
                    $('#errorContainer').hide();
                    $('#patientInfo').show();
                    $('#patientName').text(response.content[0].patientName);
                    $('#patientPhone').text(response.content[0].patientMobile);
                    $('#age').text(response.content[0].age);
                    var dateStr = response.content[0].date;
                    var dateObj = new Date(dateStr);
                    var formattedDate = dateObj.toLocaleString('en-US', { timeZone: 'UTC' }).replace(/, /, ' ');
                    $('#date').text(formattedDate);

                } else {
                    $('#errorContainer').show();
                    $('#recordsTable').hide();
                    $('#successMessage').hide();
                }
            },
            error: function () {
                console.error('Error fetching records.');
                $('#errorContainer').show();
                $('#recordsTable').hide();
                $('#successMessage').hide();
            }
        });
    });

    function displayRecords(records) {
        $('#recordsTable').empty();
        var tableHtml = '<table class="table"><thead><tr><th>#</th><th>Lab No</th><th>Test</th><th>Status</th><th>File</th></tr></thead><tbody>';
        records.forEach(function (record, index) {
            var statusBadge = record.status === 1 ? '<span class="badge badge-success">Completed</span>' : '<span class="badge badge-warning">Pending</span>';
            const pdfPath = JSON.parse(record.file).filePath;
            const imageUrl = 'http://localhost:8080/report/' + encodeURIComponent(pdfPath);
            tableHtml += '<tr><td>' + (index + 1) + '</td><td>' + record.labNo + '</td><td>' + record.test + '</td><td>' + statusBadge + '</td><td><a href="' + imageUrl + '" class="btn btn-info" target="_blank">Download</a></td></tr>';
        });
        tableHtml += '</tbody></table>';
        $('#recordsTable').html(tableHtml).show();
    }
});
