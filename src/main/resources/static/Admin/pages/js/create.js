$(document).ready(function(){
    (function($) {
        "use strict";
    jQuery.validator.addMethod('answercheck', function (value, element) {
        return this.optional(element) || /^\bcat\b$/.test(value)
    }, "type the correct answer -_-");

    $(function() {
            $('#createTest').validate({
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
                            $('#successMessage').text('New Test Created Successfully !').fadeIn();
                            form.reset();
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
            $('#createTechnician').validate({
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
                    $.ajax({
                        type: "POST",
                        url: "http://localhost:8080/technician/store",
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function(response) {
                            $('#successMessage').text('New Technician Created Successfully!').fadeIn();
                            form.reset();
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
            $('#createReport').validate({
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
                    $.ajax({
                        type: "POST",
                        url: "http://localhost:8080/report/store",
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function(response) {
                            $('#successMessage').text('New Report Created Successfully!').fadeIn();
                            form.reset();
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
        $(function() {
            $('#createReport').validate({
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
                    $.ajax({
                        type: "POST",
                        url: "http://localhost:8080/report/store",
                        data: formData,
                        processData: false,
                        contentType: false,
                        success: function(response) {
                            $('#successMessage').text('New Report Created Successfully!').fadeIn();
                            form.reset();
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

        $(function() {
            $('#createUser').validate({
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
                        name: $('#name').val(),
                        address: $('#address').val(),
                        phoneNumber: $('#phoneNumber').val(),
                        email: $('#email').val(),
                        role: $('#role').val(),
                        username: $('#username').val(),
                        password: $('#password').val()
                    };
                    $.ajax({
                        type: "POST",
                        url: "http://localhost:8080/user/store",
                        data: JSON.stringify(formData),
                        contentType: "application/json",
                        success: function(response) {
                            $('#successMessage').text('New User Created Successfully!').fadeIn();
                            form.reset();
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

 })(jQuery)
})