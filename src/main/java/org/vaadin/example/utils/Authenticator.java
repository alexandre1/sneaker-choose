package org.vaadin.example.utils;

import org.vaadin.example.entity.Person;

import java.util.Optional;

public class Authenticator{
	
	public Optional <Person> authenticate (String username , String password ) {
        Person user = new Person (username, password);
        return Optional.of( user );
    }
}
