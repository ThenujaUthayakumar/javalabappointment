$(document).ready(function(){
    (function($) {
        "use strict";
        jQuery.validator.addMethod('answercheck', function (value, element) {
            return this.optional(element) || /^\bcat\b$/.test(value);
        }, "Type the correct answer -_-");

        $(function() {
            // Function to get URL parameter by name
            function getUrlParameter(name) {
                name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
                var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
                var results = regex.exec(location.search);
                return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
            };

            // Retrieve the record ID from the URL
            var recordId = getUrlParameter('id');

            // AJAX call to get record data based on record ID
            $.ajax({
                url: "http://localhost:8080/test?id=" + recordId,
                type: "GET",
                success: function(data) {
                    // Populate form fields with retrieved data
                    $('#testId').val(recordId);
                    $('#name').val(data.name);
                    $('#description').val(data.description);
                    $('#cost').val(data.cost);
                },
                error: function(xhr, status, error) {
                    console.error(xhr.responseText);
                }
            });

            $('#updateTest').validate({
                rules: {
                    name: {
                        required: true,
                        minlength: 2
                    },
                    description: {
                        required: true,
                        minlength: 1
                    },
                    cost: {
                        required: true,
                        minlength: 2
                    }
                },
                messages: {
                    name: {
                        required: "Please enter Test Name!",
                        minlength: "Test name must consist of at least 2 characters"
                    },
                    description: {
                        required: "Please enter Test Description!",
                        minlength: "Test description must consist of at least 1 character"
                    },
                    cost: {
                        required: "Please enter Test Cost!",
                        minlength: "Test cost must consist of at least 2 characters"
                    }
                },
                errorClass: "text-danger",
                submitHandler: function(form) {
                    var formData = {
                        id: $('#testId').val(),
                        name: $('#name').val(),
                        description: $('#description').val(),
                        cost: $('#cost').val()
                    };

                    $.ajax({
                        type: "PUT",
                        contentType: "application/json",
                        url: "http://localhost:8080/test?id=" + recordId,
                        data: JSON.stringify(formData),
                        success: function(response) {
                            $('#successMessage').text('Test Updated Successfully!').fadeIn();
                            setTimeout(function() {
                                window.location.href = "../test.html";
                            }, 2000);
                        },
                        error: function(xhr, status, error) {
                            console.error(xhr.responseText);
                        }
                    });
                    return false;
                }
            });
        });
    })(jQuery);
});
