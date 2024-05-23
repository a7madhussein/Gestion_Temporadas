package com.standings.ui.page;

import java.util.HashMap;



public class LoginValidationUtil {
	
	public static final int ALL_FIELDS_ARE_EMPTY = 1;
	public static final int EMAIL_FIELD_IS_EMPTY = 2;
	public static final int PASSWORD_FIELD_IS_EMPTY = 3; 
	public static final int EMAIL_NOT_FOUND = 4;  
	public static final int INCORRECT_PASSWORD = 5;
	public static final int ALL_CHECKS_PASSED = 6;
	
	
    
	//REQUIRES: userEmail and userPassword musn't be a null value.
	//EFFECTS : returns the appropriate number case based on the given case.
	
	public static int validateLogin(HashMap<String, String> loginInfo, String userEmail, String userPassword) {
	    if (userEmail.isEmpty() && userPassword.isEmpty()) {
	        return ALL_FIELDS_ARE_EMPTY;
	    } else if (userEmail.isEmpty()) {
	        return EMAIL_FIELD_IS_EMPTY;
	    } else if (userPassword.isEmpty()) {
	        return PASSWORD_FIELD_IS_EMPTY;
	    } else if (!loginInfo.containsKey(userEmail)) {
	        return EMAIL_NOT_FOUND;
	    } else {
	        // Hashear la contraseña proporcionada
	        String hashedPassword = UserManager.hashPassword(userPassword);

	        // Comparar con la contraseña hasheada almacenada
	        if (!loginInfo.get(userEmail).equals(hashedPassword)) {
	            return INCORRECT_PASSWORD;
	        }
	    }

	    return ALL_CHECKS_PASSED;
	}

    	
 }

