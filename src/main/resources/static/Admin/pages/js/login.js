$(document).ready(function(){
    (function($) {
        "use strict";
        jQuery.validator.addMethod('answercheck', function (value, element) {
            return this.optional(element) || /^\bcat\b$/.test(value)
        }, "type the correct answer -_-");

        $(function() {
            $('#login').validate({
                rules: {
                    username: {
                        required: true,
                        minlength: 2
                    },
                    password: {
                        required: true,
                        minlength: 1
                    }
                },
                messages: {
                    username: {
                        required: "Please Enter User Name !",
                        minlength: "Your name must consist of at least 2 characters"
                    },
                    password: {
                        required: "Please Enter Password !",
                        minlength: "Your Password must consist of at least 4 characters"
                    }
                },
                errorClass: "text-danger",
                submitHandler: function(form) {
                    var formData = {
                        username: $('#username').val(),
                        password: $('#password').val()
                    };
                    $.ajax({
                        type: "POST",
                        contentType: "application/json",
                        url: "http://localhost:8080/user/login",
                        data: JSON.stringify(formData),
                        success: function(response) {
                            $('#successMessage').text('Login Successfully !').fadeIn();
                            $('#errorMessage').hide();
                            form.reset();
                            setTimeout(function() {
                                window.location.href = "pages/dashboard.html";
                            }, 2000);
                        },
                        error: function(xhr, status, error) {
                            var errorMessage = JSON.parse(xhr.responseText);
                            $('#errorMessage').text('Please Enter Correct Username & Password !').fadeIn();
                            $('#successMessage').hide();
                        }
                    });
                    return false;
                }
            });
        });
    })(jQuery)
})
