$(document).ready(function(){
    (function($) {
        "use strict";
        jQuery.validator.addMethod('answercheck', function (value, element) {
            return this.optional(element) || /^\bcat\b$/.test(value);
        }, "Type the correct answer -_-");

        $(function() {
            function getUrlParameter(name) {
                name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
                var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
                var results = regex.exec(location.search);
                return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
            };
            var recordId = getUrlParameter('id');
            $.ajax({
                url: "http://localhost:8080/test?id=" + recordId,
                type: "GET",
                success: function(data) {
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

        $(function() {
            function getUrlParameter(name) {
                name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
                var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
                var results = regex.exec(location.search);
                return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
            };
            var recordId = getUrlParameter('id');
            $.ajax({
                url: "http://localhost:8080/user?id=" + recordId,
                type: "GET",
                success: function(data) {
                    $('#userId').val(recordId);
                    $('#name').val(data.name),
                    $('#address').val(data.address),
                    $('#phoneNumber').val(data.phoneNumber),
                    $('#email').val(data.email),
                    $('#role').val(data.role),
                    $('#username').val(data.username),
                    $('#password').val(data.password)
                },
                error: function(xhr, status, error) {
                    console.error(xhr.responseText);
                }
            });

            $('#updateUser').validate({
                rules: {
                    name: {
                        required: true,
                        minlength: 1
                    },
                    address: {
                        required: true,
                        minlength: 1
                    },
                    phoneNumber: {
                        required: true,
                        minlength: 1
                    },
                    email: {
                        required: true,
                        email: true
                    },
                    role: {
                        required: true,
                        minlength: 1
                    },
                    username: {
                        required: true,
                        minlength: 1
                    },
                    password: {
                        required: true,
                        minlength: 1
                    },
                    confirm: {
                        required: true,
                        minlength: 1,
                        equalTo: "#password"
                    }
                },
                messages: {
                    name: {
                        required: "Please Enter Name!",
                        minlength: "Your name must consist of at least 1 characters"
                    },
                    address: {
                        required: "Please Enter Address!",
                        minlength: "Your subject must consist of at least 1 characters"
                    },
                    phoneNumber: {
                        required: "Please Enter Mobile Number!",
                        minlength: "Your number must consist of at least 1 characters"
                    },
                    email: {
                        required: "Please Enter E-mail!",
                        email: "Please enter a valid email address"
                    },
                    role: {
                        required: "Please Enter Role!",
                        minlength: "Your number must consist of at least 1 characters"
                    },
                    username: {
                        required: "Please Enter Username!",
                        minlength: "Your number must consist of at least 1 characters"
                    },
                    password: {
                        required: "Please Enter password!",
                        minlength: "Your number must consist of at least 1 characters"
                    },
                    confirm: {
                        required: "Please Enter Correct Confirm Password!",
                        minlength: "Your number must consist of at least 1 characters",
                        equalTo: "Password and Confirm Password must match"
                    }
                },
                errorClass: "text-danger",
                submitHandler: function(form) {
                    var formData = {
                        id: $('#userId').val(),
                        name: $('#name').val(),
                        address: $('#address').val(),
                        phoneNumber: $('#phoneNumber').val(),
                        email: $('#email').val(),
                        role: $('#role').val(),
                        username: $('#username').val(),
                        password: $('#password').val()
                    };

                    $.ajax({
                        type: "PUT",
                        contentType: "application/json",
                        url: "http://localhost:8080/user?id=" + recordId,
                        data: JSON.stringify(formData),
                        success: function(response) {
                            $('#successMessage').text('User Updated Successfully!').fadeIn();
                            setTimeout(function() {
                                window.location.href = "../user.html";
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

        $(function() {
            function getUrlParameter(name) {
                name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
                var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
                var results = regex.exec(location.search);
                return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
            };
            var recordId = getUrlParameter('id');
            $.ajax({
                url: "http://localhost:8080/technician?id=" + recordId,
                type: "GET",
                success: function(data) {
                    $('#technicianId').val(recordId);
                    $('#name').val(data.name),
                    $('#address').val(data.address),
                    $('#phoneNumber').val(data.phoneNumber),
                    $('#email').val(data.email),
                    $('#position').val(data.position),
                    $('#joinDate').val(data.joinDate),
                    $('#file').val(data.file)
                },
                error: function(xhr, status, error) {
                    console.error(xhr.responseText);
                }
            });

            $('#updateTechnician').validate({
                rules: {
                    name: {
                        required: true,
                        minlength: 2
                    },
                    phoneNumber: {
                        required: true,
                        minlength: 1
                    },
                    address: {
                        required: true,
                        minlength: 2
                    },
                    email: {
                        required: true,
                        minlength: 2
                    },
                    position: {
                        required: true,
                        minlength: 2
                    },
                    joinDate: {
                        required: true,
                        minlength: 2
                    },
                    file: {
                        required: true
                    }
                },
                messages: {
                    name: {
                        required: "Please Enter Name!",
                        minlength: "Your name must consist of at least 2 characters"
                    },
                    phoneNumber: {
                        required: "Please Enter Mobile Number!",
                        minlength: "Your subject must consist of at least 4 characters"
                    },
                    address: {
                        required: "Please Enter Address!",
                        minlength: "Your number must consist of at least 5 characters"
                    },
                    email: {
                        required: "Please Enter E-mail!",
                        minlength: "Your number must consist of at least 5 characters"
                    },
                    position: {
                        required: "Please Enter Position!",
                        minlength: "Your number must consist of at least 5 characters"
                    },
                    joinDate: {
                        required: "Please Enter Join Date!",
                        minlength: "Your number must consist of at least 5 characters"
                    },
                    file: {
                        required: "Please select a file to upload!"
                    }
                },
                errorClass: "text-danger",
                submitHandler: function(form) {
                    var formData = new FormData(form);
                    formData.append("id", $('#technicianId').val());

                    $.ajax({
                        type: "PUT",
                        url: "http://localhost:8080/technician?id=" + recordId,
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function(response) {
                            $('#successMessage').text('Technician Updated Successfully!').fadeIn();
                            setTimeout(function() {
                                window.location.href = "../technician.html";
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

        $(function() {
            function getUrlParameter(name) {
                name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
                var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
                var results = regex.exec(location.search);
                return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
            };
            var recordId = getUrlParameter('id');
            $.ajax({
                url: "http://localhost:8080/report?id=" + recordId,
                type: "GET",
                success: function(data) {
                    $('#reportId').val(recordId);
                    $('#technicianId').val(data.technicianId.id),
                    $('#appointmentId').val(data.appointmentId.id),
                    $('#status').val(data.status),
                    $('#file').val(data.file)
                },
                error: function(xhr, status, error) {
                    console.error(xhr.responseText);
                }
            });

            $('#updateReport').validate({
                rules: {
                    technicianId: {
                        required: true,
                        minlength: 1
                    },
                    appointmentId: {
                        required: true,
                        minlength: 1
                    },
                    status: {
                        required: true,
                        minlength: 1
                    },
                    file: {
                        required: true
                    }
                },
                messages: {
                    technicianId: {
                        required: "Please Enter Technician Name!",
                        minlength: "Your name must consist of at least 1 characters"
                    },
                    appointmentId: {
                        required: "Please Enter Patient Name!",
                        minlength: "Your subject must consist of at least 1 characters"
                    },
                    status: {
                        required: "Please Enter Status!",
                        minlength: "Your number must consist of at least 1 characters"
                    },
                    file: {
                        required: "Please select a file to upload!"
                    }
                },
                errorClass: "text-danger",
                submitHandler: function(form) {
                    var formData = new FormData(form);
                    formData.append("id", $('#reportId').val());

                    $.ajax({
                        type: "PUT",
                        url: "http://localhost:8080/report?id=" + recordId,
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function(response) {
                            $('#successMessage').text('Report Updated Successfully!').fadeIn();
                            setTimeout(function() {
                                window.location.href = "../report.html";
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
