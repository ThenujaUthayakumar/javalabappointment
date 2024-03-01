$(document).ready(function(){
    (function($) {
        "use strict";
    jQuery.validator.addMethod('answercheck', function (value, element) {
        return this.optional(element) || /^\bcat\b$/.test(value)
    }, "type the correct answer -_-");

    $(function() {
        $('#contactForm').validate({
            rules: {
                name: {
                    required: true,
                    minlength: 2
                },
                phoneNumber: {
                    required: true,
                    minlength: 4
                },
                address: {
                    required: true,
                    minlength: 5
                },
                message: {
                    required: true,
                    minlength: 20
                }
            },
            messages: {
                name: {
                    required: "Please Enter Your Name !",
                    minlength: "your name must consist of at least 2 characters"
                },
                phoneNumber: {
                    required: "Please Enter Your Mobile Number !",
                    minlength: "your subject must consist of at least 4 characters"
                },
                address: {
                    required: "Please Enter Your Address !",
                    minlength: "your Number must consist of at least 5 characters"
                },
                message: {
                    required: "Please Enter Your Message or Inquiry !",
                    minlength: "thats all? really?"
                }
            },
            errorClass: "text-danger",
            submitHandler: function(form) {
                var formData = {
                    name: $('#name').val(),
                    phoneNumber: $('#phoneNumber').val(),
                    address: $('#address').val(),
                    message: $('#message').val()
                };
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: "http://localhost:8080/contact/store",
                    data: JSON.stringify(formData),
                    success: function(response) {
                        $('#successMessage').text('Your Message Send Successfully !');
                        $('#successMessage').fadeIn();
                        $(form)[0].reset();
                    },
                    error: function() {
                        $('#contactForm').fadeTo("slow", 1, function() {
                            $('#error').fadeIn();
                            $('.modal').modal('hide');
                            $('#error').modal('show');
                        });
                    }
                });
            }
        })
    })
 })(jQuery)
})