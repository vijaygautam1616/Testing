package JudicialBranch;

import java.util.Calendar;
import java.util.Date;

public class test {

	public static void main(String[] args) {
		Calendar c=Calendar.getInstance();
		  c.setTime(c.getTime());
		  Date FromDate=c.getTime();
		   c.add(Calendar.DATE,7);
		   Date toDate=c.getTime();
		   System.out.println(FromDate);
		 System.out.println(toDate);
		if(FromDate.compareTo(toDate)<0){
			  System.out.println("Within the range");
		  }
	}

}
