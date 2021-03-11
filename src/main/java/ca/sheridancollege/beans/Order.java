package ca.sheridancollege.beans;

import java.io.Serializable;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Order implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -6588703325359702201L;
	private long orderId;
	private String movieName;
	private String movieDate;
	private String movieTime;
	private int movieSeat;
	
}
