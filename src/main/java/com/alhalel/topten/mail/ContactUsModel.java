package com.alhalel.topten.mail;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class ContactUsModel {

    @NotEmpty
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 5, message = "Subject must be at least 5 characters long")
    private String subject;

    @NotEmpty
    @Size(min = 20, message = "Message must be at least 20 characters long")
    private String message;
}
