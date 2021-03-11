package ca.sheridancollege.beans;

import java.io.Serializable;
import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Price implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7449133715132825153L;
	public long price;
}
