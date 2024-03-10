$(document).ready(function(){
    (function($) {
        "use strict";
    jQuery.validator.addMethod('answercheck', function (value, element) {
        return this.optional(element) || /^\bcat\b$/.test(value)
    }, "type the correct answer -_-");

    $(function() {
                $.ajax({
                    url: 'http://localhost:8080/test',
                    method: 'GET',
                    success: function(data) {
                        var optionsHtml = '<option disabled>Select Test</option>';
                        data.content.forEach(function(test) {
                            optionsHtml += '<option value="' + test.id + '">' + test.name + '</option>';
                        });
                        $('#testId').html(optionsHtml);
                    },
                    error: function() {
                        console.error('Error fetching test data');
                    }
                });

/*--------------------------------- ONLINE APPOINTMENT -----------------------------*/
        $('#appointmentForm').validate({
            rules: {
                name: {
                    required: true,
                    minlength: 2
                },
                phoneNumber: {
                    required: true,
                    minlength: 2
                },
                address: {
                    required: true,
                    minlength: 2
                },
                email: {
                    required: true,
                    minlength: 2
                },
                 age: {
                     required: true,
                     minlength: 2
                 },
                doctorName: {
                    required: true,
                    minlength: 2
                },
                gender: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "Please Enter Patient Name !",
                    minlength: "Please Enter Patient Name !"
                },
                phoneNumber: {
                    required: "Please Enter Patient Mobile Number !",
                    minlength: "Please Enter Patient Mobile Number !"
                },
                address: {
                    required: "Please Enter Patient Address !",
                    minlength: "Please Enter Patient Address !"
                },
                email: {
                    required: "Please Enter Patient E-mail !",
                    minlength: "Please Enter Patient E-mail !"
                },
                age: {
                    required: "Please Enter Patient Age !",
                    minlength: "Please Enter Patient Age !"
                },
                doctorName: {
                    required: "Please Enter Suggestion Doctor Name !",
                    minlength: "Please Enter Suggestion Doctor Name !"
                }
            },
            errorClass: "text-danger",
            submitHandler: function(form) {
                var formData = {
                    name: $('#name').val(),
                    phoneNumber: $('#phoneNumber').val(),
                    address: $('#address').val(),
                    email: $('#email').val(),
                    age: $('#age').val(),
                    doctorName: $('#doctorName').val(),
                    gender: $('#gender').val(),
                    testId: { id: $('#testId').val() },
                    appointmentDateTime: $('#appointmentDateTime').val()
                };
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: "http://localhost:8080/appointment/store",
                    data: JSON.stringify(formData),
                    success: function(response) {
                        $('#confirmation').show();
                        //$('#appointment').hide();
                        $('#appointmentId').text(response.id);
                        $('#paymentButton').data('appointmentId', response.id);
                        console.log('Stored Appointment ID:', response.id);
                        $('#paymentButton').data('testId', response.testId);
                        console.log('Stored Test ID:', response.testId);
                       // $('#successMessage').fadeIn();
                        $(form)[0].reset();
                    },
                    error: function() {
                        $('#appointmentForm').fadeTo("slow", 1, function() {
                            $('#error').fadeIn();
                            $('.modal').modal('hide');
                            $('#error').modal('show');
                        });
                    }
                });
            }
        })

/*-------------------------------- ONLINE PAYMENT ----------------------------*/
$('#paymentButton').click(function(event) {
    var appointmentId = $(this).data('appointmentId');
    var testId = $(this).data('testId');

    if (!appointmentId) {
        console.error('Invalid Appointment ID');
        return;
    }

    if (!testId) {
        console.error('Invalid Test ID');
        return;
    }

    if (validatePaymentDetails()) {
        // Fetch test data to get the amount
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/test?id=" + testId.id,
            success: function(testData) {
                // Assuming the response contains an 'amount' field
                var amount = testData.amount;

                var formData = {
                    appointmentId: { id: appointmentId },
                    cardHolderName: $('#cardHolderName').val(),
                    cardHolderPhoneNumber: $('#cardHolderPhoneNumber').val(),
                    cardNumber: parseInt($('#cardNumber').val()),
                    cvv: parseInt($('#cvv').val()),
                    expiryDate: $('#expiryDate').val(),
                    amount: amount
                };

                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: "http://localhost:8080/payment/store",
                    data: JSON.stringify(formData),
                    success: function(response) {
                        $('#successMessage').text('Your Appointment Successfully & You Will Get Your Payment Receipt Via Your E-mail !');
                        $('#successMessage').fadeIn();
                        $('#paymentButton')[0].reset();
                    },
                    error: function() {
                        $('#paymentForm').fadeTo("slow", 1, function() {
                            $('#error').fadeIn();
                            $('.modal').modal('hide');
                            $('#error').modal('show');
                        });
                    }
                });
            },
            error: function() {
                console.error('Failed to fetch test details');
            }
        });
    }
});


    function validatePaymentDetails() {
        var valid = true;
        // Payment validation logic
        var cardHolderName = $('#cardHolderName').val();
        var cardHolderPhoneNumber = $('#cardHolderPhoneNumber').val();
        var cardNumber = $('#cardNumber').val();
        var cvv = $('#cvv').val();
        var expiryDate = $('#expiryDate').val();
       // var amount = $('#amount').val();

        if (!cardHolderName || !cardHolderPhoneNumber || !cardNumber || !cvv || !expiryDate || !amount) {
            valid = false;
            alert('Please fill out all payment details.');
        } else {
            // Additional validation logic if needed
        }

        return valid;
    }
     })
 })(jQuery);
});
