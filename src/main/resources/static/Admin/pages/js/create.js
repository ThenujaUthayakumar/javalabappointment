        $(document).ready(function() {
            // Initialize form validation
            $('#createTest').validate({
                rules: {
                    name: {
                        required: true,
                        minlength: 2
                    },
                    description: {
                        required: true,
                        minlength: 2
                    },
                    cost: {
                        required: true,
                        minlength: 2
                    }
                },
                messages: {
                    name: {
                        required: "Please Enter Test Name !",
                        minlength: "Your name must consist of at least 2 characters"
                    },
                    description: {
                        required: "Please Enter Test Description !",
                        minlength: "Your subject must consist of at least 4 characters"
                    },
                    cost: {
                        required: "Please Enter Test Cost !",
                        minlength: "Your number must consist of at least 5 characters"
                    }
                },
                errorClass: "text-danger",
                submitHandler: function(form) {
                    // Form submission logic
                    var formData = {
                        name: $('#name').val(),
                        description: $('#description').val(),
                        cost: $('#cost').val()
                    };
                    $.ajax({
                        type: "POST",
                        contentType: "application/json",
                        url: "http://localhost:8080/test/store",
                        data: JSON.stringify(formData),
                        success: function(response) {
                            // Handle success response
                            $('#successMessage').text('New Test Created Successfully !').fadeIn();
                            form.reset();
                        },
                        error: function(xhr, status, error) {
                            // Handle error response
                            console.error(xhr.responseText);
                        }
                    });
                    return false; // Prevent default form submission
                }
            });
        });