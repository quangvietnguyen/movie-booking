package ca.sheridancollege.beans;

import java.io.Serializable;
import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Data

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2236104756441383295L;
	private long userId;
	private String userName;
	private String encryptedPassword;
	private int role;
}
